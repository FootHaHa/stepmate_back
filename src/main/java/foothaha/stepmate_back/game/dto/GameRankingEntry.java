package foothaha.stepmate_back.game.dto;

import foothaha.stepmate_back.game.entity.GameScore;
import lombok.Getter;

@Getter
public class GameRankingEntry {
    private final int rank;
    private final String nickname;
    private final String profileImageUrl;
    private final int score;
    private final int playTimeSeconds;
    private final String grade;
    private final boolean isMe;

    public GameRankingEntry(int rank, GameScore gs, String myEmail) {
        this.rank = rank;
        this.nickname = gs.getUser().getName();
        this.profileImageUrl = gs.getUser().getProfileImageUrl();
        this.score = gs.getScore();
        this.playTimeSeconds = gs.getPlayTimeSeconds();
        this.grade = gs.getRank();
        this.isMe = gs.getUser().getEmail().equals(myEmail);
    }
}
