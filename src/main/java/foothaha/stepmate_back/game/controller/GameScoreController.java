package foothaha.stepmate_back.game.controller;

import foothaha.stepmate_back.game.dto.GameRankingEntry;
import foothaha.stepmate_back.game.dto.GameScoreRequest;
import foothaha.stepmate_back.game.service.GameScoreService;
import foothaha.stepmate_back.response.CommonResponse;
import foothaha.stepmate_back.response.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameScoreController {

    private final GameScoreService gameScoreService;

    // 점수 저장
    @PostMapping("/scores")
    public ResponseEntity<CommonResponse<Void>> saveScore(
            Authentication authentication,
            @RequestBody GameScoreRequest request) {
        gameScoreService.saveScore(authentication.getName(), request);
        return ResponseEntity.ok(ResponseBuilder.success(null));
    }

    // 전체 랭킹 (gameMode별 점수 순)
    @GetMapping("/ranking")
    public ResponseEntity<CommonResponse<List<GameRankingEntry>>> getRanking(
            Authentication authentication,
            @RequestParam(required = false, defaultValue = "rhythmGame") String gameMode) {
        List<GameRankingEntry> ranking = gameScoreService.getRanking(authentication.getName(), gameMode);
        return ResponseEntity.ok(ResponseBuilder.success(ranking));
    }

    // 내 베스트 기록
    @GetMapping("/my-best")
    public ResponseEntity<CommonResponse<List<GameRankingEntry>>> getMyBest(
            Authentication authentication) {
        List<GameRankingEntry> myBest = gameScoreService.getMyBest(authentication.getName());
        return ResponseEntity.ok(ResponseBuilder.success(myBest));
    }
}
