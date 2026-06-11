package foothaha.stepmate_back.game.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameScoreRequest {
    private Integer score;
    private Integer playTimeSeconds;
    private Integer perfectCount;
    private Integer goodCount;
    private Integer missCount;
    private Integer postureScore;
    private String rank;
    private String gameMode;
}
