package foothaha.stepmate_back.sensor.service;

import foothaha.stepmate_back.run.entity.RunSession;
import foothaha.stepmate_back.run.repository.RunSessionRepository;
import foothaha.stepmate_back.sensor.dto.RawDataBatchRequest;
import foothaha.stepmate_back.sensor.entity.SensorRawData;
import foothaha.stepmate_back.sensor.repository.SensorRawDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SensorRawDataService {

    private final SensorRawDataRepository sensorRawDataRepository;
    private final RunSessionRepository runSessionRepository;

    @Transactional
    public void saveRawData(String email, RawDataBatchRequest request) {
        RunSession session = runSessionRepository.findById(request.getRunSessionId())
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다."));

        if (!session.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("본인의 세션에만 데이터를 저장할 수 있습니다.");
        }

        List<SensorRawData> entities = request.getData().stream()
                .map(item -> SensorRawData.builder()
                        .runSession(session)
                        .stepNumber(item.getStepNumber())
                        .footSide(item.getFootSide())
                        .measuredAt(item.getMeasuredAt() != null ? LocalDateTime.parse(item.getMeasuredAt()) : null)
                        .pressure1(item.getPressure1())
                        .pressure2(item.getPressure2())
                        .pressure3(item.getPressure3())
                        .pressure4(item.getPressure4())
                        .pressure5(item.getPressure5())
                        .accelX(item.getAccelX())
                        .accelY(item.getAccelY())
                        .accelZ(item.getAccelZ())
                        .gyroX(item.getGyroX())
                        .gyroY(item.getGyroY())
                        .gyroZ(item.getGyroZ())
                        .build())
                .toList();

        sensorRawDataRepository.saveAll(entities);
    }
}
