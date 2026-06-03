package foothaha.stepmate_back.run.dto;

import foothaha.stepmate_back.run.entity.SessionSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SessionSummaryResponse {

    private Long summaryId;
    private Long runSessionId;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer durationSeconds;

    private Integer totalSteps;
    private Double calories;

    private Double avgHLLeft;
    private Double avgM5Left;
    private Double avgM1Left;
    private Double avgHeelLeft;
    private Double avgMFLeft;

    private Double avgHLRight;
    private Double avgM5Right;
    private Double avgM1Right;
    private Double avgHeelRight;
    private Double avgMFRight;

    private Double avgLeftPressure;
    private Double avgRightPressure;

    private Double balanceScore;

    public static SessionSummaryResponse from(SessionSummary summary) {
        return SessionSummaryResponse.builder()
                .summaryId(summary.getSummaryId())
                .runSessionId(summary.getRunSession().getSessionId())
                .startedAt(summary.getRunSession().getStartedAt())
                .endedAt(summary.getRunSession().getEndedAt())
                .durationSeconds(summary.getRunSession().getDurationSeconds())
                .totalSteps(summary.getTotalSteps())
                .calories(summary.getCalories())
                .avgHLLeft(summary.getAvgHLLeft())
                .avgM5Left(summary.getAvgM5Left())
                .avgM1Left(summary.getAvgM1Left())
                .avgHeelLeft(summary.getAvgHeelLeft())
                .avgMFLeft(summary.getAvgMFLeft())
                .avgHLRight(summary.getAvgHLRight())
                .avgM5Right(summary.getAvgM5Right())
                .avgM1Right(summary.getAvgM1Right())
                .avgHeelRight(summary.getAvgHeelRight())
                .avgMFRight(summary.getAvgMFRight())
                .avgLeftPressure(summary.getAvgLeftPressure())
                .avgRightPressure(summary.getAvgRightPressure())
                .balanceScore(summary.getBalanceScore())
                .build();
    }
}