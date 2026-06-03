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

    // 총 걸음 수
    private Integer totalSteps;

//    추후 추가
//    총 거리 (m)
//    private Double totalDistanceMeters;
//
//    // 평균 페이스 (초/km)
//    private Double averagePace;

    // 소모 칼로리
    private Double calories;

    // =========================
    // 압력 정보
    // =========================

    // 평균값
    private Double avgHeelLeft;
    private Double avgMidLeft;
    private Double avgToeLeft;
    private Double avgOuterLeft;
    private Double avgMeta1Left;

    private Double avgHeelRight;
    private Double avgMidRight;
    private Double avgToeRight;
    private Double avgOuterRight;
    private Double avgMeta1Right;

    private Double avgLeftPressure;
    private Double avgRightPressure;

    private Double balanceScore;

}