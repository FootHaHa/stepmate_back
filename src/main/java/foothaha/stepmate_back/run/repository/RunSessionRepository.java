package foothaha.stepmate_back.run.repository;

import foothaha.stepmate_back.run.entity.RunSession;
import foothaha.stepmate_back.run.entity.RunSessionStatus;
import foothaha.stepmate_back.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RunSessionRepository extends JpaRepository<RunSession, Long> {

    List<RunSession> findByUserOrderByStartedAtDesc(User user);

    @Query("SELECT DISTINCT CAST(s.startedAt AS date) FROM RunSession s " +
           "WHERE s.user = :user AND YEAR(s.startedAt) = :year AND MONTH(s.startedAt) = :month " +
           "AND s.status = :status")
    List<LocalDate> findDistinctDatesByUserAndYearAndMonth(
            @Param("user") User user,
            @Param("year") int year,
            @Param("month") int month,
            @Param("status") RunSessionStatus status);

    @Query("SELECT s FROM RunSession s LEFT JOIN FETCH s.sessionSummary " +
           "WHERE s.user = :user AND s.startedAt >= :start AND s.startedAt < :end " +
           "AND s.status = :status ORDER BY s.startedAt ASC")
    List<RunSession> findByUserAndDateWithSummary(
            @Param("user") User user,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") RunSessionStatus status);
}