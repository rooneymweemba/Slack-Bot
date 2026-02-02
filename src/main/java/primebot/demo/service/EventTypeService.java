package primebot.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primebot.demo.DTOs.CreateEventTypeRequest;
import primebot.demo.model.EventType;
import primebot.demo.model.EventTypeProperties;
import primebot.demo.repository.EventTypePropertiesRepository;
import primebot.demo.repository.EventTypeRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EventTypeService {

    private final EventTypeRepository repository;
    private final EventTypePropertiesRepository eventTypePropertiesRepository;

    public EventTypeService(EventTypeRepository repository, EventTypePropertiesRepository eventTypePropertiesRepository) {
        this.eventTypePropertiesRepository = eventTypePropertiesRepository;
        this.repository = repository;
    }

    @Transactional
    public EventType create(CreateEventTypeRequest request) {
        if (request.getEventTypePropertyId() == null) {
            throw new IllegalArgumentException("eventTypePropertyId is required");
        }
        EventTypeProperties props = eventTypePropertiesRepository.findById(request.getEventTypePropertyId())
                .orElseThrow(() -> new IllegalArgumentException("EventTypeProperties not found"));
        EventType eventType = new EventType();
        eventType.setName(request.getName());
        eventType.setDescription(request.getDescription());
        eventType.setProperties(props);
        log.info(
                "Creating EventType name={}, description={}, propertyId={}",
                request.getName(),
                request.getDescription(),
                request.getEventTypePropertyId()
        );        return repository.save(eventType);
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