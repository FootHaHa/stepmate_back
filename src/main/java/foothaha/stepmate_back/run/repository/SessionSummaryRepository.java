package foothaha.stepmate_back.run.repository;

import foothaha.stepmate_back.run.entity.SessionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionSummaryRepository extends JpaRepository<SessionSummary, Long> {

    Optional<SessionSummary> findByRunSession_SessionId(Long runSessionId);

    void deleteByRunSession_SessionId(Long runSessionId);
}