package foothaha.stepmate_back.auth.dto;

import foothaha.stepmate_back.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에 돌려주는 사용자 정보. 내부 필드(password 등)는 노출하지 않는다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String email;
    private String name;
    private String profileImageUrl;
    private Double weightKg;
    private Double heightCm;
    private String provider;
    private String token;

    public static UserResponse from(User user, String token) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .weightKg(user.getWeightKg())
                .heightCm(user.getHeightCm())
                .provider(user.getProvider() == null ? null : user.getProvider().name())
                .token(token)
                .build();
    }
}
