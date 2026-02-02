package primebot.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import primebot.demo.model.EventType;

import java.util.List;
@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {

    @Query("""
SELECT et FROM EventType et
JOIN FETCH et.properties
JOIN FETCH et.slackEvents
""")
    List<EventType> findAllWithPropertiesAndEvents();
}
