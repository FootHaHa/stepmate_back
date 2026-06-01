package foothaha.stepmate_back.run.entity;

import foothaha.stepmate_back.base.BaseEntity;
import foothaha.stepmate_back.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "run_session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RunSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    // 측정한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 측정 시작 시간
    @Column
    private LocalDateTime startedAt;

    // 측정 종료 시간
    private LocalDateTime endedAt;

    // 초 단위 운동 시간
    private Integer durationSeconds;

    // 세션 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RunSessionStatus status;

    @OneToOne(mappedBy = "runSession", fetch = FetchType.LAZY)
    private SessionSummary sessionSummary;

    public void finish(LocalDateTime startedAt, LocalDateTime endedAt, Integer durationSeconds) {
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.durationSeconds = durationSeconds;
        this.status = RunSessionStatus.FINISHED;
    }
}
