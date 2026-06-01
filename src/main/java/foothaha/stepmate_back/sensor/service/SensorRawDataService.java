package foothaha.stepmate_back.sensor.service;

import foothaha.stepmate_back.sensor.repository.SensorRawDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SensorRawDataService {

    private final SensorRawDataRepository sensorRawDataRepository;
}