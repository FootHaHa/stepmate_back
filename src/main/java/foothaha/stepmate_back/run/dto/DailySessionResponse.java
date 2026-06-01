package foothaha.stepmate_back.run.dto;

import foothaha.stepmate_back.run.entity.RunSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class DailySessionResponse {

    private Long runSessionId;
    private LocalDateTime startedAt;
    private Integer durationSeconds;
    private Integer totalSteps;

    public static DailySessionResponse from(RunSession session) {
        Integer steps = session.getSessionSummary() != null
                ? session.getSessionSummary().getTotalSteps()
                : null;

        return DailySessionResponse.builder()
                .runSessionId(session.getSessionId())
                .startedAt(session.getStartedAt())
                .durationSeconds(session.getDurationSeconds())
                .totalSteps(steps)
                .build();
    }
}