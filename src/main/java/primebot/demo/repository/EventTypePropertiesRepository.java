package primebot.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import primebot.demo.model.EventTypeProperties;

public interface EventTypePropertiesRepository extends JpaRepository<EventTypeProperties, Long> {
}
