package foothaha.stepmate_back.sensor.dto;

import foothaha.stepmate_back.sensor.entity.FootSide;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SensorRawDataRequest {

    private Integer stepNumber;
    private FootSide footSide;

    private String measuredAt;

    private Integer pressure1;
    private Integer pressure2;
    private Integer pressure3;
    private Integer pressure4;
    private Integer pressure5;

    private Double accelX;
    private Double accelY;
    private Double accelZ;

    private Double gyroX;
    private Double gyroY;
    private Double gyroZ;
}
