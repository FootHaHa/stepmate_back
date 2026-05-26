package foothaha.stepmate_back.user.controller;
import foothaha.stepmate_back.response.CommonResponse;
import foothaha.stepmate_back.response.ResponseBuilder;
import foothaha.stepmate_back.user.dto.UserResponse;
import foothaha.stepmate_back.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<UserResponse>> getMyInfo(Authentication authentication) {
        String email = authentication.getName();
        UserResponse response = userService.findMyInfo(email);
        return ResponseEntity.ok(ResponseBuilder.success(response));
    }
}
