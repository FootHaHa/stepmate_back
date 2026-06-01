package foothaha.stepmate_back.run.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlySessionResponse {
    private List<LocalDate> dates;
}