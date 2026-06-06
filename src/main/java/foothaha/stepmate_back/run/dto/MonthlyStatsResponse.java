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
}