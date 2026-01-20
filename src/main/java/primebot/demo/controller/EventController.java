package primebot.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import primebot.demo.model.SlackEvent;
import primebot.demo.service.SlackEventService;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final SlackEventService eventService;
    @Autowired
    public EventController(SlackEventService eventService) {
        this.eventService = eventService;
    }
    @PatchMapping("/update/{id}")
    public void update(@PathVariable Long id) {
        eventService.updateSlackEvent(id);
    }
    @PostMapping("/create")
    public SlackEvent create(@RequestBody SlackEvent event) {
        return eventService.createEvent(event);
    }

    @GetMapping("/all")
    public List<SlackEvent> getAllEvents() {
        return eventService.getAllSlackEvents();
    }

    @GetMapping("/get/{id}")
    public SlackEvent getById(@PathVariable Long id) {
        return eventService.getSlackEventById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        eventService.deleteSlackEvent(id);
    }
}

