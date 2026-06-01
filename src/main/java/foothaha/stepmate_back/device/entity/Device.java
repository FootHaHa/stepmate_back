package foothaha.stepmate_back.device.entity;

import foothaha.stepmate_back.base.BaseEntity;
import foothaha.stepmate_back.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "device")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Device extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * BLE MAC Address 또는 UUID
     */
    @Column(nullable = false, unique = true)
    private String identifier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FootSide footSide;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus status;
}