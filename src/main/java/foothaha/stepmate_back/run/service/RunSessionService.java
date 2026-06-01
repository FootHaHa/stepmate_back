package foothaha.stepmate_back.run.service;

import foothaha.stepmate_back.run.dto.RunSessionStartResponse;
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

    @Transactional
    public RunSessionStartResponse startRun(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        RunSession session = RunSession.builder()
                .user(user)
                .startedAt(LocalDateTime.now())
                .status(RunSessionStatus.IN_PROGRESS)
                .build();

        return RunSessionStartResponse.from(runSessionRepository.save(session));
    }

    @Transactional
    public void finishRun(String email, Long runSessionId, Integer totalSteps) {
        RunSession session = runSessionRepository.findById(runSessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다."));

        if (!session.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("본인의 세션만 종료할 수 있습니다.");
        }

        session.finish(LocalDateTime.now());
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

        double avgHeelLeft   = avg(leftData,  SensorRawData::getPressure1);
        double avgMidLeft    = avg(leftData,  SensorRawData::getPressure2);
        double avgToeLeft    = avg(leftData,  SensorRawData::getPressure3);
        double avgOuterLeft  = avg(leftData,  SensorRawData::getPressure4);

        double avgHeelRight  = avg(rightData, SensorRawData::getPressure1);
        double avgMidRight   = avg(rightData, SensorRawData::getPressure2);
        double avgToeRight   = avg(rightData, SensorRawData::getPressure3);
        double avgOuterRight = avg(rightData, SensorRawData::getPressure4);

        double avgLeftPressure  = (avgHeelLeft  + avgMidLeft  + avgToeLeft  + avgOuterLeft)  / 4.0;
        double avgRightPressure = (avgHeelRight + avgMidRight + avgToeRight + avgOuterRight) / 4.0;

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
                .avgHeelRight(avgHeelRight)
                .avgMidRight(avgMidRight)
                .avgToeRight(avgToeRight)
                .avgOuterRight(avgOuterRight)
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

    private double calcBalanceScore(double left, double right) {
        double total = left + right;
        if (total == 0) return 100.0;
        double leftRatio = left / total;
        // 50/50이 100점, 한쪽으로 쏠릴수록 감소
        return 100.0 - Math.abs(leftRatio - 0.5) * 200.0;
    }
}