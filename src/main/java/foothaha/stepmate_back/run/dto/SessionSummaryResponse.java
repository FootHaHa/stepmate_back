package foothaha.stepmate_back.run.dto;

import foothaha.stepmate_back.run.entity.LandingType;
import foothaha.stepmate_back.run.entity.SessionSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SessionSummaryResponse {

    private Long summaryId;
    private Long runSessionId;

    private String startedAt;
    private String endedAt;
    private Integer durationSeconds;

    private Integer totalSteps;
    private Double totalDistanceKm;
    private Double averagePace;
    private Double calories;

    private Double avgT1Left;
    private Double avgM5Left;
    private Double avgM1Left;
    private Double avgHeelLeft;
    private Double avgMFLeft;

    private Double avgT1Right;
    private Double avgM5Right;
    private Double avgM1Right;
    private Double avgHeelRight;
    private Double avgMFRight;

    private Double avgLeftPressure;
    private Double avgRightPressure;

    private Double balanceScore;

    private LandingType leftLandingType;
    private LandingType rightLandingType;

    private Long uphillSeconds;
    private Long downhillSeconds;
    private Long flatSeconds;

    public static SessionSummaryResponse from(SessionSummary summary) {
        return SessionSummaryResponse.builder()
                .summaryId(summary.getSummaryId())
                .runSessionId(summary.getRunSession().getSessionId())
                .startedAt(summary.getRunSession().getStartedAt() != null ? summary.getRunSession().getStartedAt().toString() : null)
                .endedAt(summary.getRunSession().getEndedAt() != null ? summary.getRunSession().getEndedAt().toString() : null)
                .durationSeconds(summary.getRunSession().getDurationSeconds())
                .totalSteps(summary.getTotalSteps())
                .totalDistanceKm(summary.getTotalDistanceKm())
                .averagePace(summary.getAveragePace())
                .calories(summary.getCalories())
                .avgT1Left(summary.getAvgT1Left())
                .avgM5Left(summary.getAvgM5Left())
                .avgM1Left(summary.getAvgM1Left())
                .avgHeelLeft(summary.getAvgHeelLeft())
                .avgMFLeft(summary.getAvgMFLeft())
                .avgT1Right(summary.getAvgT1Right())
                .avgM5Right(summary.getAvgM5Right())
                .avgM1Right(summary.getAvgM1Right())
                .avgHeelRight(summary.getAvgHeelRight())
                .avgMFRight(summary.getAvgMFRight())
                .avgLeftPressure(summary.getAvgLeftPressure())
                .avgRightPressure(summary.getAvgRightPressure())
                .balanceScore(summary.getBalanceScore())
                .leftLandingType(summary.getLeftLandingType())
                .rightLandingType(summary.getRightLandingType())
                .uphillSeconds(summary.getUphillSeconds())
                .downhillSeconds(summary.getDownhillSeconds())
                .flatSeconds(summary.getFlatSeconds())
                .build();
    }
}