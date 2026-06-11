package foothaha.stepmate_back.run.dto;

import foothaha.stepmate_back.run.entity.LandingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MonthlyStatsResponse {

    private LandingType avgLeftLandingType;
    private LandingType avgRightLandingType;
    private Long totalDurationSeconds;
    private Double totalDistanceKm;
    private Double averagePace;
    private Double totalCalories;

    // 30일 평균 센서 압력 [T1, M5, M1, Heel, MF] × 좌우
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
}