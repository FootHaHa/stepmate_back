package foothaha.stepmate_back.sensor.controller;

import foothaha.stepmate_back.response.CommonResponse;
import foothaha.stepmate_back.response.ResponseBuilder;
import foothaha.stepmate_back.sensor.dto.RawDataBatchRequest;
import foothaha.stepmate_back.sensor.service.SensorRawDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensor")
@RequiredArgsConstructor
public class SensorRawDataController {

    private final SensorRawDataService sensorRawDataService;

    @PostMapping("/batch")
    public ResponseEntity<CommonResponse<Void>> saveRawData(
            Authentication authentication,
            @RequestBody RawDataBatchRequest request
    ) {
        sensorRawDataService.saveRawData(authentication.getName(), request);
        return ResponseEntity.ok(ResponseBuilder.success(null));
    }


}