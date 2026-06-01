package foothaha.stepmate_back.sensor.controller;

import foothaha.stepmate_back.sensor.service.SensorRawDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensor")
@RequiredArgsConstructor
public class SensorRawDataController {

    private final SensorRawDataService sensorRawDataService;
}