package foothaha.stepmate_back.user.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBodyInfoRequest {

    @NotNull(message = "weightKg is required.")
    @DecimalMin(value = "1.0", message = "weightKg must be at least 1kg.")
    @DecimalMax(value = "500.0", message = "weightKg must be at most 500kg.")
    private Double weightKg;

    @NotNull(message = "heightCm is required.")
    @DecimalMin(value = "50.0", message = "heightCm must be at least 50cm.")
    @DecimalMax(value = "250.0", message = "heightCm must be at most 250cm.")
    private Double heightCm;

}
