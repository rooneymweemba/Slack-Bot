package primebot.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primebot.demo.DTOs.CreateEventTypeRequest;
import primebot.demo.exceptions.InvalidRequestException;
import primebot.demo.exceptions.ResourceNotFoundException;
import primebot.demo.exceptions.ServiceException;
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
            throw new InvalidRequestException("eventTypePropertyId is required");
        }
        EventTypeProperties props = eventTypePropertiesRepository.findById(request.getEventTypePropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("EventTypeProperties not found"));
        EventType eventType = new EventType();
        eventType.setName(request.getName());
        eventType.setDescription(request.getDescription());
        eventType.setProperties(props);
        log.info(
                "Creating EventType name={}, description={}, propertyId={}",
                request.getName(),
                request.getDescription(),
                request.getEventTypePropertyId()
        );
        try {
            return repository.save(eventType);
        }catch (DataAccessException e){
            log.error("Error saving EventType", e);
            throw new ServiceException("Failed to create EventType");
        }
    }

    @Transactional(readOnly = true)
    public Optional<EventType> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (DataAccessException ex) {
            log.error("Failed to retrieve EventType with id {}", id, ex);
            throw new ServiceException("Failed to retrieve EventType", ex);
        }
    }

    @Transactional
    public Optional<EventType> update(Long id, EventType incoming) {
        try {
            return repository.findById(id)
                    .map(existing -> {
                        existing.setName(incoming.getName());
                        existing.setDescription(incoming.getDescription());
                        return repository.save(existing);
                    });
        } catch (DataAccessException ex) {
            log.error("Failed to update EventType with id {}", id, ex);
            throw new ServiceException("Failed to update EventType", ex);
        }
    }

    @Transactional
    public boolean delete(Long id) {
        try {
            if (!repository.existsById(id)) {
                return false;
            }
            repository.deleteById(id);
            return true;
        } catch (DataAccessException ex) {
            log.error("Failed to delete EventType with id {}", id, ex);
            throw new ServiceException("Failed to delete EventType", ex);
        }
    }
    @Transactional
    public List<EventType> findAll() {
        try {
            return repository.findAll();
        } catch (DataAccessException ex) {
            log.error("Failed to retrieve all EventTypes", ex);
            throw new ServiceException("Failed to retrieve EventTypes", ex);
        }
    }
}