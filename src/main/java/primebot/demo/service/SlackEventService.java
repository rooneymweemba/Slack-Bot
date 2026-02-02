package primebot.demo.service;

import org.springframework.stereotype.Service;
import primebot.demo.model.SlackEvent;
import primebot.demo.repository.SlackEventRepository;
import jakarta.persistence.EntityNotFoundException;


import java.util.List;

@Service
public class SlackEventService {

    private final SlackEventRepository repository;

    public SlackEventService(SlackEventRepository repository) {
        this.repository = repository;
    }

    public SlackEvent createEvent(SlackEvent event) {

        if (repository.existsByName(event.getName())) {
            throw new EntityNotFoundException("Event already exists");
        }

        return repository.save(event);
    }
    public List<SlackEvent> getAllSlackEvents() {
        return repository.findAll();
    }
    public SlackEvent getSlackEventById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }
    public void deleteSlackEvent(Long id) {
        repository.deleteById(id);
    }
    public void updateSlackEvent(Long id) {
        SlackEvent event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setNotified(false);
        repository.save(event);
    }



}