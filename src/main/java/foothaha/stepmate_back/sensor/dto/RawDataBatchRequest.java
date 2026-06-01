package foothaha.stepmate_back.sensor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RawDataBatchRequest {

    private Long runSessionId;
    private List<SensorRawDataRequest> data;
}