package foothaha.stepmate_back.run.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinishSessionRequest {
    Long runSessionId;
    Integer totalSteps;

    String startedAt;

    String endedAt;

    Integer durationSeconds;
}
