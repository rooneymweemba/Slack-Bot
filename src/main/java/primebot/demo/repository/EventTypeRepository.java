package primebot.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import primebot.demo.model.EventType;
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
}
