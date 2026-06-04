package foothaha.stepmate_back.run.dto;

import foothaha.stepmate_back.run.entity.RunSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DailySessionResponse {

    private Long runSessionId;
    private String startedAt;
    private Integer durationSeconds;
    private Integer totalSteps;

    public static DailySessionResponse from(RunSession session) {
        Integer steps = session.getSessionSummary() != null
                ? session.getSessionSummary().getTotalSteps()
                : null;

        return DailySessionResponse.builder()
                .runSessionId(session.getSessionId())
                .startedAt(session.getStartedAt() != null ? session.getStartedAt().toString() : null)
                .durationSeconds(session.getDurationSeconds())
                .totalSteps(steps)
                .build();
    }
}