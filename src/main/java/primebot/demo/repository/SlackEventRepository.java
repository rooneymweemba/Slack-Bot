package primebot.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import primebot.demo.model.EventType;
import primebot.demo.model.SlackEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SlackEventRepository extends JpaRepository<SlackEvent, Long> {
    @Query("""
        SELECT e
        FROM EventType e
        JOIN e.properties p
        WHERE
            (
                p.scheduleType = 'ONE_TIME'
                AND p.notifyAt <= :now
            )
            OR
            (
                p.scheduleType = 'RECURRING'
                AND p.startDateTime <= :now
                AND (p.endDateTime IS NULL OR p.endDateTime >= :now)
                )
    """)
    List<EventType> findEventsToTrigger(
            @Param("now") LocalDateTime now,
            @Param("todayStart") LocalDateTime todayStart
    );

    boolean existsByName(String name);
    List<SlackEvent> findByType(String type);


}
