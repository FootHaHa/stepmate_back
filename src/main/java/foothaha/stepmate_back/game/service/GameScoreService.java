package foothaha.stepmate_back.game.service;

import foothaha.stepmate_back.game.dto.GameRankingEntry;
import foothaha.stepmate_back.game.dto.GameScoreRequest;
import foothaha.stepmate_back.game.entity.GameScore;
import foothaha.stepmate_back.game.repository.GameScoreRepository;
import foothaha.stepmate_back.user.entity.User;
import foothaha.stepmate_back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameScoreService {

    private final GameScoreRepository gameScoreRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveScore(String email, GameScoreRequest req) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        gameScoreRepository.save(GameScore.builder()
                .user(user)
                .score(req.getScore())
                .playTimeSeconds(req.getPlayTimeSeconds())
                .perfectCount(req.getPerfectCount())
                .goodCount(req.getGoodCount())
                .missCount(req.getMissCount())
                .postureScore(req.getPostureScore())
                .rank(req.getRank())
                .gameMode(req.getGameMode())
                .build());
    }

    @Transactional(readOnly = true)
    public List<GameRankingEntry> getRanking(String myEmail, String gameMode) {
        List<GameScore> scores = (gameMode != null && !gameMode.isBlank())
                ? gameScoreRepository.findTopByGameMode(gameMode)
                : gameScoreRepository.findTopByPlayTime();
        List<GameRankingEntry> result = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            result.add(new GameRankingEntry(i + 1, scores.get(i), myEmail));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<GameRankingEntry> getMyBest(String email) {
        List<GameScore> scores = gameScoreRepository.findMyBest(email);
        List<GameRankingEntry> result = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            result.add(new GameRankingEntry(i + 1, scores.get(i), email));
        }
        return result;
    }
}
