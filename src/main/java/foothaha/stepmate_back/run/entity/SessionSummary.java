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

    // =========================
    // 운동 기본 정보
    // =========================

    private Integer totalSteps;
    private Double totalDistanceKm;
    private Double averagePace;
    private Double calories;

    // =========================
    // 압력 정보
    // =========================

    // 평균값
    private Double avgT1Left;
    private Double avgM5Left;
    private Double avgM1Left;
    private Double avgHeelLeft;
    private Double avgMFLeft;

    private Double avgT1Right;
    private Double avgM5Right;
    private Double avgM1Right;
    private Double avgHeelRight;
    private Double avgMFRight;

    private Double avgLeftPressure;
    private Double avgRightPressure;

    private Double balanceScore;

    @Enumerated(EnumType.STRING)
    private LandingType leftLandingType;

    @Enumerated(EnumType.STRING)
    private LandingType rightLandingType;

    // =========================
    // 경사 구간 시간
    // =========================

    private Long uphillSeconds;
    private Long downhillSeconds;
    private Long flatSeconds;

}