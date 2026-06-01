package foothaha.stepmate_back.run.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FinishSessionRequest {
    Long runSessionId;
    Integer totalSteps;
    LocalDateTime startedAt;
    LocalDateTime endedAt;
    Integer durationSeconds;
}
