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

    private Double avgHeelLeft;
    private Double avgMidLeft;
    private Double avgToeLeft;
    private Double avgOuterLeft;

    private Double avgHeelRight;
    private Double avgMidRight;
    private Double avgToeRight;
    private Double avgOuterRight;

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
                .avgHeelLeft(summary.getAvgHeelLeft())
                .avgMidLeft(summary.getAvgMidLeft())
                .avgToeLeft(summary.getAvgToeLeft())
                .avgOuterLeft(summary.getAvgOuterLeft())
                .avgHeelRight(summary.getAvgHeelRight())
                .avgMidRight(summary.getAvgMidRight())
                .avgToeRight(summary.getAvgToeRight())
                .avgOuterRight(summary.getAvgOuterRight())
                .avgLeftPressure(summary.getAvgLeftPressure())
                .avgRightPressure(summary.getAvgRightPressure())
                .balanceScore(summary.getBalanceScore())
                .build();
    }
}