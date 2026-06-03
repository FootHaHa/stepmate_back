package foothaha.stepmate_back.run.service;

import foothaha.stepmate_back.run.dto.DailySessionResponse;
import foothaha.stepmate_back.run.dto.MonthlySessionResponse;
import foothaha.stepmate_back.run.dto.RunSessionStartResponse;
import foothaha.stepmate_back.run.dto.SessionSummaryResponse;
import foothaha.stepmate_back.run.entity.RunSession;
import foothaha.stepmate_back.run.entity.RunSessionStatus;
import foothaha.stepmate_back.run.entity.SessionSummary;
import foothaha.stepmate_back.run.repository.RunSessionRepository;
import foothaha.stepmate_back.run.repository.SessionSummaryRepository;
import foothaha.stepmate_back.sensor.entity.FootSide;
import foothaha.stepmate_back.sensor.entity.SensorRawData;
import foothaha.stepmate_back.sensor.repository.SensorRawDataRepository;
import foothaha.stepmate_back.user.entity.User;
import foothaha.stepmate_back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RunSessionService {

    private final RunSessionRepository runSessionRepository;
    private final SessionSummaryRepository sessionSummaryRepository;
    private final SensorRawDataRepository sensorRawDataRepository;
    private final UserRepository userRepository;

    public MonthlySessionResponse getMonthlySessions(String email, int year, int month) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<LocalDate> dates = runSessionRepository.findDistinctDatesByUserAndYearAndMonth(
                user, year, month, RunSessionStatus.FINISHED);
        return new MonthlySessionResponse(dates);
    }

    public List<DailySessionResponse> getDailySessions(String email, LocalDate date) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<RunSession> sessions = runSessionRepository.findByUserAndDateWithSummary(
                user,
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay(),
                RunSessionStatus.FINISHED);
        return sessions.stream().map(DailySessionResponse::from).toList();
    }

    public SessionSummaryResponse getSummary(Long runSessionId) {
        SessionSummary summary = sessionSummaryRepository.findByRunSession_SessionId(runSessionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 세션의 요약 정보를 찾을 수 없습니다."));
        return SessionSummaryResponse.from(summary);
    }

    @Transactional
    public RunSessionStartResponse startRun(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        RunSession session = RunSession.builder()
                .user(user)
                .status(RunSessionStatus.IN_PROGRESS)
                .build();

        return RunSessionStartResponse.from(runSessionRepository.save(session));
    }

    @Transactional
    public void finishRun(String email, Long runSessionId, Integer totalSteps,
                          LocalDateTime startedAt, LocalDateTime endedAt, Integer durationSeconds) {
        RunSession session = runSessionRepository.findById(runSessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다."));

        if (!session.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("본인의 세션만 종료할 수 있습니다.");
        }

        session.finish(startedAt, endedAt, durationSeconds);
        createSummaries(session, totalSteps);
    }

    private void createSummaries(RunSession session, int totalSteps) {
        List<SensorRawData> allData = sensorRawDataRepository.findByRunSession_SessionId(session.getSessionId());

        List<SensorRawData> leftData = allData.stream()
                .filter(d -> d.getFootSide() == FootSide.LEFT)
                .toList();
        List<SensorRawData> rightData = allData.stream()
                .filter(d -> d.getFootSide() == FootSide.RIGHT)
                .toList();

        double avgToeLeft    = avg(leftData,  SensorRawData::getPressure1);
        double avgMidLeft    = avg(leftData,  SensorRawData::getPressure2);
        double avgOuterLeft  = avg(leftData,  SensorRawData::getPressure3);
        double avgHeelLeft   = avg(leftData,  SensorRawData::getPressure4);
        double avgMeta1Left  = avg(leftData,  SensorRawData::getPressure5);

        double avgToeRight   = avg(rightData, SensorRawData::getPressure1);
        double avgMidRight   = avg(rightData, SensorRawData::getPressure2);
        double avgOuterRight = avg(rightData, SensorRawData::getPressure3);
        double avgHeelRight  = avg(rightData, SensorRawData::getPressure4);
        double avgMeta1Right = avg(rightData, SensorRawData::getPressure5);

        double avgLeftPressure  = (avgHeelLeft  + avgMidLeft  + avgToeLeft  + avgOuterLeft  + avgMeta1Left)  / 5.0;
        double avgRightPressure = (avgHeelRight + avgMidRight + avgToeRight + avgOuterRight + avgMeta1Right) / 5.0;

        double balanceScore = calcBalanceScore(avgLeftPressure, avgRightPressure);
        // 몸무게 추가해야 함
        double calories = totalSteps * 0.04;

        SessionSummary summary = SessionSummary.builder()
                .runSession(session)
                .totalSteps(totalSteps)
                .calories(calories)
                .avgHeelLeft(avgHeelLeft)
                .avgMidLeft(avgMidLeft)
                .avgToeLeft(avgToeLeft)
                .avgOuterLeft(avgOuterLeft)
                .avgMeta1Left(avgMeta1Left)
                .avgHeelRight(avgHeelRight)
                .avgMidRight(avgMidRight)
                .avgToeRight(avgToeRight)
                .avgOuterRight(avgOuterRight)
                .avgMeta1Right(avgMeta1Right)
                .avgLeftPressure(avgLeftPressure)
                .avgRightPressure(avgRightPressure)
                .balanceScore(balanceScore)
                .build();

        sessionSummaryRepository.save(summary);
    }

    private double avg(List<SensorRawData> data, java.util.function.Function<SensorRawData, Integer> getter) {
        return data.stream()
                .map(getter)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    @Transactional
    public void deleteSession(String email, Long runSessionId) {
        RunSession session = runSessionRepository.findById(runSessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다."));

        if (!session.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("본인의 세션만 삭제할 수 있습니다.");
        }

        sensorRawDataRepository.deleteAllByRunSession_SessionId(runSessionId);
        sessionSummaryRepository.deleteByRunSession_SessionId(runSessionId);
        runSessionRepository.delete(session);
    }

    private double calcBalanceScore(double left, double right) {
        double total = left + right;
        if (total == 0) return 100.0;
        double leftRatio = left / total;
        // 50/50이 100점, 한쪽으로 쏠릴수록 감소
        return 100.0 - Math.abs(leftRatio - 0.5) * 200.0;
    }
}