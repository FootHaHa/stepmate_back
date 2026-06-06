package foothaha.stepmate_back.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreakResponse {

    private int currentStreak;
    private int maxStreak;
    private boolean ranToday;

}
