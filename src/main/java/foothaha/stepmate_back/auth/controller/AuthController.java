package foothaha.stepmate_back.auth.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import foothaha.stepmate_back.auth.dto.GoogleLoginRequest;
import foothaha.stepmate_back.auth.dto.UserResponse;
import foothaha.stepmate_back.auth.service.GoogleTokenVerifier;
import foothaha.stepmate_back.config.JWTUtil;
import foothaha.stepmate_back.response.CommonResponse;
import foothaha.stepmate_back.response.ResponseBuilder;
import foothaha.stepmate_back.response.ResponseCode;
import foothaha.stepmate_back.user.entity.User;
import foothaha.stepmate_back.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/google")
    public ResponseEntity<CommonResponse<UserResponse>> loginWithGoogle(@RequestBody @Valid GoogleLoginRequest request) {
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(request.getIdToken());
        if (payload == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseBuilder.error(ResponseCode.UNAUTHORIZED, "Google ID Token 검증에 실패했습니다."));
        }

        String providerId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        if (email == null || providerId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseBuilder.error(ResponseCode.BAD_REQUEST, "Google ID Token 에 email/sub 클레임이 없습니다."));
        }

        User user = userService.upsertGoogleUser(providerId, email, name, picture);
        log.info("Google 로그인 완료 userId={}, email={}", user.getUserId(), user.getEmail());

        String token = jwtUtil.createJwt(user.getEmail(), "ROLE_USER", 60 * 60 * 10 * 1000L);

        return ResponseEntity.ok(ResponseBuilder.success(UserResponse.from(user, token)));
    }
}
