package foothaha.stepmate_back.run.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TodaySummaryResponse {

    private Double totalDistanceKm;
    private Double averagePace;
    private Double averageBalanceScore;
}