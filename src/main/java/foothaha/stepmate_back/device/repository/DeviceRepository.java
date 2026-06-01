package foothaha.stepmate_back.device.repository;

import foothaha.stepmate_back.device.entity.Device;
import foothaha.stepmate_back.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByUser(User user);

    Optional<Device> findByIdentifier(String identifier);
}
