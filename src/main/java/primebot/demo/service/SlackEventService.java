package primebot.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import primebot.demo.exceptions.InvalidRequestException;
import primebot.demo.exceptions.ResourceNotFoundException;
import primebot.demo.exceptions.ServiceException;
import primebot.demo.model.SlackEvent;
import primebot.demo.repository.SlackEventRepository;



import java.util.List;
@Slf4j
@Service
public class SlackEventService {

    private final SlackEventRepository repository;

    public SlackEventService(SlackEventRepository repository) {

        this.repository = repository;
    }

    public SlackEvent createEvent(SlackEvent event) {
        if (event == null) {
            throw new InvalidRequestException("Event payload is required");
        }
        if (event.getName() == null || event.getName().isBlank()) {
            throw new InvalidRequestException("Event name is required");
        }

        try {
            if (repository.existsByName(event.getName())) {
                throw new InvalidRequestException("Event already exists with name: " + event.getName());
            }
            log.info("Creating SlackEvent name={}", event.getName());
            return repository.save(event);
        } catch (DataAccessException ex) {
            log.error("Failed to save SlackEvent name={}", event.getName(), ex);
            throw new ServiceException("Failed to create SlackEvent", ex);
        }
    }
    public List<SlackEvent> getAllSlackEvents() {
        try {
            return repository.findAll();
        } catch (DataAccessException ex) {
            log.error("Failed to retrieve Slack events", ex);
            throw new ServiceException("Failed to retrieve Slack events", ex);
        }
    }
    public SlackEvent getSlackEventById(Long id) {
        if (id == null) {
            throw new InvalidRequestException("id is required");
        }
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("SlackEvent not found with id: " + id));
        } catch (DataAccessException ex) {
            log.error("Failed to retrieve SlackEvent with id {}", id, ex);
            throw new ServiceException("Failed to retrieve SlackEvent", ex);
        }
    }
    public void deleteSlackEvent(Long id) {
        if (id == null) {
            throw new InvalidRequestException("id is required");
        }
        try {
            if (!repository.existsById(id)) {
                throw new ResourceNotFoundException("SlackEvent not found with id: " + id);
            }
            repository.deleteById(id);
            log.info("Deleted SlackEvent id={}", id);
        } catch (DataAccessException ex) {
            log.error("Failed to delete SlackEvent with id {}", id, ex);
            throw new ServiceException("Failed to delete SlackEvent", ex);
        }
    }
    public void updateSlackEvent(Long id) {
        if (id == null) {
            throw new InvalidRequestException("id is required");
        }
        try{
        SlackEvent event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setNotified(false);
        repository.save(event);
        } catch (DataAccessException ex) {
            log.error("Failed to update SlackEvent with id {}", id, ex);
            throw new ServiceException("Failed to update SlackEvent", ex);
        }
    }



}