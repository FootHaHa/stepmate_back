package foothaha.stepmate_back.game.repository;

import foothaha.stepmate_back.game.entity.GameScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameScoreRepository extends JpaRepository<GameScore, Long> {

    // 게임 모드별 점수 기준 상위 50명 랭킹
    // rhythmGame은 구 모드값(interval/highTempo/lowTempo)도 포함
    @Query("""
        SELECT g FROM GameScore g
        JOIN FETCH g.user
        WHERE g.gameMode = :gameMode
           OR (:gameMode = 'rhythmGame' AND g.gameMode IN ('interval', 'highTempo', 'lowTempo'))
        ORDER BY g.score DESC
        LIMIT 50
    """)
    List<GameScore> findTopByGameMode(@Param("gameMode") String gameMode);

    // 플레이 시간 합산 기준 상위 50명 랭킹 (하위 호환)
    @Query("""
        SELECT g FROM GameScore g
        JOIN FETCH g.user
        ORDER BY g.playTimeSeconds DESC
        LIMIT 50
    """)
    List<GameScore> findTopByPlayTime();

    // 내 최고 기록 (플레이 시간 기준)
    @Query("""
        SELECT g FROM GameScore g
        WHERE g.user.email = :email
        ORDER BY g.playTimeSeconds DESC
        LIMIT 10
    """)
    List<GameScore> findMyBest(String email);
}
