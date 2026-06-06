package foothaha.stepmate_back.user.entity;

import foothaha.stepmate_back.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * StepMate 사용자.
 * Google 로그인 등 OAuth 공급자로 식별되는 사용자를 다룬다.
 * - email + provider 조합으로 사용자를 식별한다.
 * - providerId 는 공급자(Google)가 발급한 고유 ID (Google의 'sub' 값).
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"provider", "provider_id"})
        }
)
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    /** 표시 이름 (Google displayName) */
    private String name;

    @Column(name = "profile_image_url", length = 1024)
    private String profileImageUrl;

    @Builder.Default
    @Column(name = "weight_kg", nullable = false, columnDefinition = "double precision default 60.0")
    private Double weightKg = 60.0;

    @Builder.Default
    @Column(name = "height_cm", nullable = false, columnDefinition = "double precision default 170.0")
    private Double heightCm = 170.0;

    @Builder.Default
    @Column(name = "current_streak", nullable = false, columnDefinition = "integer default 0")
    private Integer currentStreak = 0;

    @Builder.Default
    @Column(name = "max_streak", nullable = false, columnDefinition = "integer default 0")
    private Integer maxStreak = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    /** 공급자가 발급한 고유 ID (Google의 sub) */
    @Column(name = "provider_id", length = 128)
    private String providerId;

}
