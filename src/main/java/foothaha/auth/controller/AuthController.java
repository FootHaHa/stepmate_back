package foothaha.auth.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import foothaha.auth.dto.GoogleLoginRequest;
import foothaha.auth.dto.UserResponse;
import foothaha.auth.service.GoogleTokenVerifier;
import foothaha.user.entity.User;
import foothaha.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final UserService userService;

    /**
     * Google 로그인.
     * 프론트엔드: POST /api/auth/google
     *   body: { "idToken": "..." }
     * 응답: { userId, email, name, profileImageUrl, provider }
     */
    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody @Valid GoogleLoginRequest request) {
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(request.getIdToken());
        if (payload == null) {
            return ResponseEntity.status(401).body(new ErrorResponse("INVALID_ID_TOKEN",
                    "Google ID Token 검증에 실패했습니다."));
        }

        String providerId = payload.getSubject();             // Google sub
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        if (email == null || providerId == null) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_PAYLOAD",
                    "Google ID Token 에 email/sub 클레임이 없습니다."));
        }

        User user = userService.upsertGoogleUser(providerId, email, name, picture);
        log.info("Google 로그인 완료 userId={}, email={}", user.getUserId(), user.getEmail());

        return ResponseEntity.ok(UserResponse.from(user));
    }

    /** 헬스체크용 ping. */
    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }

    private record ErrorResponse(String code, String message) {}
}
