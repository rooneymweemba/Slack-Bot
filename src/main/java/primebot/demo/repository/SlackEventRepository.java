package primebot.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import primebot.demo.model.SlackEvent;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SlackEventRepository extends JpaRepository<SlackEvent, Long> {

    List<SlackEvent> findByDateAndNotifiedFalse(LocalDate date);
    boolean existsByNameAndDate(String name, LocalDate date);
    List<SlackEvent> findByType(String type);

    List<SlackEvent> findByDate(LocalDate date);
}
