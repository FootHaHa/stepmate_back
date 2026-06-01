package foothaha.stepmate_back.run.controller;

import foothaha.stepmate_back.response.CommonResponse;
import foothaha.stepmate_back.response.ResponseBuilder;
import foothaha.stepmate_back.run.dto.FinishSessionRequest;
import foothaha.stepmate_back.run.dto.RunSessionStartResponse;
import foothaha.stepmate_back.run.dto.SessionSummaryResponse;
import foothaha.stepmate_back.run.service.RunSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/run")
@RequiredArgsConstructor
public class RunSessionController {

    private final RunSessionService runSessionService;

    @PostMapping
    public ResponseEntity<CommonResponse<RunSessionStartResponse>> startRun(
            Authentication authentication) {
        RunSessionStartResponse response = runSessionService.startRun(authentication.getName());
        return ResponseEntity.ok(ResponseBuilder.success(response));
    }

    @PatchMapping("/{runSessionId}/finish")
    public ResponseEntity<CommonResponse<Void>> finishRun(
            Authentication authentication,
            @RequestBody FinishSessionRequest request) {
        runSessionService.finishRun(authentication.getName(), request.getRunSessionId(), request.getTotalSteps(),
                request.getStartedAt(), request.getEndedAt(), request.getDurationSeconds());
        return ResponseEntity.ok(ResponseBuilder.success(null));
    }

    @GetMapping("/{runSessionId}/summary")
    public ResponseEntity<CommonResponse<SessionSummaryResponse>> getSummary(
            @PathVariable Long runSessionId) {
        return ResponseEntity.ok(ResponseBuilder.success(runSessionService.getSummary(runSessionId)));
    }
}