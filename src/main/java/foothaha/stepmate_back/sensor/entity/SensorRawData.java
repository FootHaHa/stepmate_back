package foothaha.stepmate_back.sensor.entity;

import foothaha.stepmate_back.run.entity.RunSession;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_raw_data")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SensorRawData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sensorRawId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_session_id", nullable = false)
    private RunSession runSession;

    // =========================
    // 걸음 순서
    // =========================
    @Column(nullable = false)
    private Integer stepNumber;

    // =========================
    // 왼발 / 오른발 판정
    // =========================
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FootSide footSide;

    // =========================
    // 측정 시간 값 (Flutter 기준)
    // =========================
    @Column(nullable = false)
    private LocalDateTime measuredAt;

    // =========================
    // 압력 센서
    // =========================
    @Column
    private Integer pressure1;

    @Column
    private Integer pressure2;

    @Column
    private Integer pressure3;

    @Column
    private Integer pressure4;

    @Column
    private Integer pressure5;

    // =========================
    // 걸음 유형 (팔자/안짱/일반)
    // =========================
    @Enumerated(EnumType.STRING)
    @Column
    private FootType footType;

    // =========================
    // 지면 각도
    // =========================
    @Column
    private Double groundAngle;

}