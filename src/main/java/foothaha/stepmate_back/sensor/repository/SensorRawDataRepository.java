package foothaha.stepmate_back.sensor.repository;

import foothaha.stepmate_back.sensor.entity.SensorRawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRawDataRepository extends JpaRepository<SensorRawData, Long> {

    List<SensorRawData> findByRunSession_SessionId(Long sessionId);

    void deleteAllByRunSession_SessionId(Long sessionId);
}