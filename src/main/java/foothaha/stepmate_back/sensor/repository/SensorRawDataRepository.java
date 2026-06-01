package foothaha.stepmate_back.sensor.repository;

import foothaha.stepmate_back.sensor.entity.SensorRawData;
import foothaha.stepmate_back.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRawDataRepository extends JpaRepository<SensorRawData, Long> {

}