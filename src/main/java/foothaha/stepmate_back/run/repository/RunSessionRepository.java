package foothaha.stepmate_back.run.repository;

import foothaha.stepmate_back.run.entity.RunSession;
import foothaha.stepmate_back.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunSessionRepository extends JpaRepository<RunSession, Long> {

    List<RunSession> findByUserOrderByStartedAtDesc(User user);
}