package primebot.demo.service;

import org.springframework.stereotype.Service;
import primebot.demo.model.EventTypeProperties;
import primebot.demo.repository.EventTypePropertiesRepository;

import java.util.List;
import java.util.Optional;
@Service
public class EventTypePropertiesService {
    private final EventTypePropertiesRepository repository;

    public EventTypePropertiesService(EventTypePropertiesRepository repository) {
        this.repository = repository;
    }

    public EventTypeProperties create(EventTypeProperties eventTypeProperties) {
        return repository.save(eventTypeProperties);
    }

    public Optional<EventTypeProperties> findById(Long id) {
        return repository.findById(id);
    }

    public List<EventTypeProperties> findAll() {
        return repository.findAll();
    }

    public Optional<EventTypeProperties> update(Long id, EventTypeProperties eventTypeProperties) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setEventType(eventTypeProperties.getEventType());
                    existing.setNotifyAt(eventTypeProperties.getNotifyAt());
                    existing.setScheduleType(eventTypeProperties.getScheduleType());
                    return repository.save(existing);
                });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }



}