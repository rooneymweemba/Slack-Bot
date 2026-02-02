package primebot.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import primebot.demo.model.EventTypeProperties;
import primebot.demo.repository.EventTypePropertiesRepository;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class EventTypePropertiesService {
    private final EventTypePropertiesRepository repository;
    public EventTypePropertiesService(EventTypePropertiesRepository repository) {
        this.repository = repository;

    }

    @Transactional
    public EventTypeProperties create(EventTypeProperties eventTypeProperties) {
        log.info("Creating EventTypeProperties: {}", eventTypeProperties);
        return repository.save(eventTypeProperties);
    }
    @Transactional
    public Optional<EventTypeProperties> findById(Long id) {
        log.info("Fetching EventTypeProperties with id: {}", id);
        return repository.findById(id);
    }
    @Transactional
    public List<EventTypeProperties> findAll() {
        log.info("Fetching all EventTypeProperties");
        return repository.findAll();
    }
    @Transactional
    public Optional<EventTypeProperties> update(Long id, EventTypeProperties eventTypeProperties) {
        Optional<EventTypeProperties> eventTypeProperties1 = repository.findById(id);
        log.info( "Updating EventTypeProperties with id: {}", id);
        return eventTypeProperties1.map(existing -> {
            if(eventTypeProperties.getStartDateTime()!=null){
                existing.setStartDateTime(eventTypeProperties.getStartDateTime());
            }
            if(eventTypeProperties.getEndDateTime()!=null) {
                existing.setEndDateTime(eventTypeProperties.getEndDateTime());
            }
            if(eventTypeProperties.getNotifyAt()!=null) {

            existing.setNotifyAt(eventTypeProperties.getNotifyAt());
            }
            if(eventTypeProperties.getScheduleType()!=null) {
                existing.setScheduleType(eventTypeProperties.getScheduleType());
            }
            return repository.save(existing);
        });
    }
    @Transactional
    public boolean delete(Long id) {
        log.info("Deleting EventTypeProperties with id: {}", id);
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }



}