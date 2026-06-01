package foothaha.stepmate_back.run.dto;

import foothaha.stepmate_back.run.entity.RunSession;
import foothaha.stepmate_back.run.entity.RunSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RunSessionStartResponse {

    private Long runSessionId;
    private LocalDateTime startedAt;
    private RunSessionStatus status;

    public static RunSessionStartResponse from(RunSession session) {
        return RunSessionStartResponse.builder()
                .runSessionId(session.getId())
                .startedAt(session.getStartedAt())
                .status(session.getStatus())
                .build();
    }
}