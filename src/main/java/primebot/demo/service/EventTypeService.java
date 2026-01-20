package primebot.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import primebot.demo.model.EventType;
import primebot.demo.repository.EventTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EventTypeService {

    private final EventTypeRepository repository;

    public EventTypeService(EventTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public EventType create(EventType eventType) {
        eventType.setId(null); // force insert
        return repository.save(eventType);
    }

    @Transactional(readOnly = true)
    public Optional<EventType> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Optional<EventType> update(Long id, EventType incoming) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(incoming.getName());
                    existing.setDescription(incoming.getDescription());
                    return repository.save(existing);
                });
    }

    @Transactional
    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
    @Transactional
    public List<EventType> findAll() {
        return repository.findAll();
    }
}