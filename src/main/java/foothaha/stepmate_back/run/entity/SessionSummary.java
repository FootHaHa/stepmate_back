package foothaha.stepmate_back.run.entity;

import foothaha.stepmate_back.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "session_summary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SessionSummary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long summaryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_session_id", nullable = false, unique = true)
    private RunSession runSession;

    // 전체 걸음 수
    private Integer totalSteps;

}