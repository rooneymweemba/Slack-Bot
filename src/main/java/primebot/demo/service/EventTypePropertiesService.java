package primebot.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import primebot.demo.exceptions.InvalidRequestException;
import primebot.demo.exceptions.ServiceException;
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
        if (eventTypeProperties == null) {
            throw new InvalidRequestException("EventTypeProperties payload is required");
        }
        log.info("Creating EventTypeProperties: {}", eventTypeProperties);
        try {
            return repository.save(eventTypeProperties);
        } catch (DataAccessException ex) {
            log.error("Failed to save EventTypeProperties", ex);
            throw new ServiceException("Failed to create EventTypeProperties", ex);
        }
    }
    @Transactional
    public Optional<EventTypeProperties> findById(Long id) {
        log.info("Fetching EventTypeProperties with id: {}", id);
        try {
            return repository.findById(id);
        } catch (DataAccessException ex) {
            log.error("Failed to retrieve EventTypeProperties with id {}", id, ex);
            throw new ServiceException("Failed to retrieve EventTypeProperties", ex);
        }
    }
    @Transactional
    public List<EventTypeProperties> findAll() {
        log.info("Fetching all EventTypeProperties");
        try {
            return repository.findAll();
        } catch (DataAccessException ex) {
            log.error("Failed to retrieve EventTypeProperties list", ex);
            throw new ServiceException("Failed to retrieve EventTypeProperties", ex);
        }
    }
    @Transactional
    public Optional<EventTypeProperties> update(Long id, EventTypeProperties eventTypeProperties) {
        if (eventTypeProperties == null) {
            throw new InvalidRequestException("EventTypeProperties payload is required for update");
        }
        try {
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
        } catch (DataAccessException ex) {
            log.error("Failed to update EventTypeProperties with id {}", id, ex);
            throw new ServiceException("Failed to update EventTypeProperties", ex);
        }
    }
    @Transactional
    public boolean delete(Long id) {
        log.info("Deleting EventTypeProperties with id: {}", id);
        try {
            if (!repository.existsById(id)) {
                return false;
            }
            repository.deleteById(id);
            return true;
        } catch (DataAccessException ex) {
            log.error("Failed to delete EventTypeProperties with id {}", id, ex);
            throw new ServiceException("Failed to delete EventTypeProperties", ex);
        }
    }



}