package foothaha.stepmate_back.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 프론트엔드에서 Google 로그인 후 ID Token 을 전달할 때 사용하는 요청 바디.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginRequest {

    @NotBlank
    private String idToken;
}
