package foothaha.stepmate_back.run.dto;

import foothaha.stepmate_back.run.entity.RunSession;
import foothaha.stepmate_back.run.entity.RunSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RunSessionStartResponse {

    private Long runSessionId;
    private String startedAt;
    private RunSessionStatus status;

    public static RunSessionStartResponse from(RunSession session) {
        return RunSessionStartResponse.builder()
                .runSessionId(session.getSessionId())
                .startedAt(session.getStartedAt() != null ? session.getStartedAt().toString() : null)
                .status(session.getStatus())
                .build();
    }
}