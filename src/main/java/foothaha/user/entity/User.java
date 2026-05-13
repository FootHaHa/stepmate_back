package foothaha.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * StepMate 사용자.
 *
 * Google 로그인 등 OAuth 공급자로 식별되는 사용자를 다룬다.
 * - email + provider 조합으로 사용자를 식별한다.
 * - providerId 는 공급자(Google)가 발급한 고유 ID (Google의 'sub' 값).
 */
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_provider", columnNames = {"provider", "provider_id"})
        }
)
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String email;

    /** 표시 이름 (Google displayName) */
    private String name;

    @Column(name = "profile_image_url", length = 1024)
    private String profileImageUrl;

    /** 인증 공급자: "GOOGLE", "LOCAL" 등 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private AuthProvider provider;

    /** 공급자가 발급한 고유 ID (Google의 sub) */
    @Column(name = "provider_id", length = 128)
    private String providerId;

    /** 로컬 가입용. OAuth 사용자는 null. (기존 컬럼 호환 유지) */
    private String password;

    /** 기존 컬럼 호환. 사용 안 함. */
    private String first_name;
    private String last_name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
