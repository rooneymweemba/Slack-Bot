package primebot.demo.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import primebot.demo.DTOs.CreateEventTypeRequest;
import primebot.demo.model.EventType;
import primebot.demo.service.EventTypeService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/eventType")
public class EventTypeController {

    private final EventTypeService service;

    public EventTypeController(EventTypeService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public EventType createEventType(@RequestBody CreateEventTypeRequest eventType) {
        System.out.println(" EVENT TYPE CREATE HIT");
        log.info("Creating EventType with name: {}, description: {}, eventTypePropertyId: {}",
                eventType.getName(), eventType.getDescription(), eventType.getEventTypePropertyId());
        return service.create(eventType);
    }

    @GetMapping("/getId/{id}")
    public ResponseEntity<EventType> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<EventType> update(
            @PathVariable Long id,
            @RequestBody EventType eventType) {

        return service.update(id, eventType)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    @GetMapping("/all")
    public List<EventType> findAll() {
        return service.findAll();
    }
}