package foothaha.stepmate_back.run.service;

import foothaha.stepmate_back.run.dto.DailySessionResponse;
import foothaha.stepmate_back.run.dto.MonthlySessionResponse;
import foothaha.stepmate_back.run.dto.RunSessionStartResponse;
import foothaha.stepmate_back.run.dto.SessionSummaryResponse;
import foothaha.stepmate_back.run.entity.LandingType;
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        double avgT1Left    = avg(leftData,  SensorRawData::getPressure1);
        double avgM5Left    = avg(leftData,  SensorRawData::getPressure2);
        double avgM1Left    = avg(leftData,  SensorRawData::getPressure3);
        double avgHeelLeft  = avg(leftData,  SensorRawData::getPressure4);
        double avgMFLeft    = avg(leftData,  SensorRawData::getPressure5);

        double avgT1Right   = avg(rightData, SensorRawData::getPressure1);
        double avgM5Right   = avg(rightData, SensorRawData::getPressure2);
        double avgM1Right   = avg(rightData, SensorRawData::getPressure3);
        double avgHeelRight = avg(rightData, SensorRawData::getPressure4);
        double avgMFRight   = avg(rightData, SensorRawData::getPressure5);

        double avgLeftPressure  = (avgT1Left  + avgM5Left  + avgM1Left  + avgHeelLeft  + avgMFLeft)  / 5.0;
        double avgRightPressure = (avgT1Right + avgM5Right + avgM1Right + avgHeelRight + avgMFRight) / 5.0;

        double balanceScore = calcBalanceScore(avgLeftPressure, avgRightPressure);
        // 몸무게 추가해야 함
        double calories = totalSteps * 0.04;

        LandingType leftLandingType  = classifyFoot(leftData);
        LandingType rightLandingType = classifyFoot(rightData);

        SessionSummary summary = SessionSummary.builder()
                .runSession(session)
                .totalSteps(totalSteps)
                .calories(calories)
                .avgT1Left(avgT1Left)
                .avgM5Left(avgM5Left)
                .avgM1Left(avgM1Left)
                .avgHeelLeft(avgHeelLeft)
                .avgMFLeft(avgMFLeft)
                .avgT1Right(avgT1Right)
                .avgM5Right(avgM5Right)
                .avgM1Right(avgM1Right)
                .avgHeelRight(avgHeelRight)
                .avgMFRight(avgMFRight)
                .avgLeftPressure(avgLeftPressure)
                .avgRightPressure(avgRightPressure)
                .balanceScore(balanceScore)
                .leftLandingType(leftLandingType)
                .rightLandingType(rightLandingType)
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

    private LandingType classifyFoot(List<SensorRawData> footData) {
        Map<Integer, List<SensorRawData>> stepGroups = footData.stream()
                .collect(Collectors.groupingBy(SensorRawData::getStepNumber));

        List<LandingType> perStepTypes = stepGroups.values().stream()
                .map(this::classifyStep)
                .toList();

        return majorityVote(perStepTypes);
    }

    private LandingType classifyStep(List<SensorRawData> readings) {
        List<SensorRawData> sorted = readings.stream()
                .sorted(Comparator.comparing(SensorRawData::getMeasuredAt))
                .toList();

        double t1Impulse = 0, m5Impulse = 0, m1Impulse = 0, mfImpulse = 0;

        for (int i = 0; i < sorted.size() - 1; i++) {
            SensorRawData curr = sorted.get(i);
            SensorRawData next = sorted.get(i + 1);
            double dt = Duration.between(curr.getMeasuredAt(), next.getMeasuredAt()).toMillis();

            t1Impulse += midpoint(curr.getPressure1(), next.getPressure1()) * dt;
            m5Impulse += midpoint(curr.getPressure2(), next.getPressure2()) * dt;
            m1Impulse += midpoint(curr.getPressure3(), next.getPressure3()) * dt;
            mfImpulse += midpoint(curr.getPressure5(), next.getPressure5()) * dt;
        }

        return classify(t1Impulse, m5Impulse, m1Impulse, mfImpulse);
    }

    private double midpoint(Integer a, Integer b) {
        return ((a == null ? 0 : a) + (b == null ? 0 : b)) / 2.0;
    }

    private LandingType classify(double t1, double m5, double m1, double mf) {
        double score1 = 0.145 * t1 + 0.270 * m1 + 0.115 * m5 + 0.091 * mf - 13.789;
        double score2 = 0.0082 * t1 + 0.120 * m1 + 0.047 * m5 + 0.348 * mf - 12.973;
        double score3 = 0.090 * t1 + 0.092 * m1 + 0.088 * m5 + 0.066 * mf -  4.422;

        if (score1 >= score2 && score1 >= score3) return LandingType.CLASS1;
        if (score2 >= score3) return LandingType.CLASS2;
        return LandingType.CLASS3;
    }

    private LandingType majorityVote(List<LandingType> types) {
        if (types.isEmpty()) return LandingType.CLASS3;
        return types.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(LandingType.CLASS3);
    }

    private double calcBalanceScore(double left, double right) {
        double total = left + right;
        if (total == 0) return 100.0;
        double leftRatio = left / total;
        // 50/50이 100점, 한쪽으로 쏠릴수록 감소
        return 100.0 - Math.abs(leftRatio - 0.5) * 200.0;
    }
}