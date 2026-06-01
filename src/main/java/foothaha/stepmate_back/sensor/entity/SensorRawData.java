package foothaha.stepmate_back.sensor.entity;

import foothaha.stepmate_back.base.BaseEntity;
import foothaha.stepmate_back.device.entity.Device;
import foothaha.stepmate_back.run.entity.RunSession;
import foothaha.stepmate_back.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_raw_data")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SensorRawData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sensorRawId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_session_id", nullable = false)
    private RunSession runSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

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

    // =========================
    // 가속도 센서
    // =========================
    @Column
    private Double accelX;

    @Column
    private Double accelY;

    @Column
    private Double accelZ;

    // =========================
    // 자이로 센서
    // =========================
    @Column
    private Double gyroX;

    @Column
    private Double gyroY;

    @Column
    private Double gyroZ;
}