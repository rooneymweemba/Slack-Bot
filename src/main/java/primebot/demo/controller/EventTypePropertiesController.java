package primebot.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import primebot.demo.model.EventType;
import primebot.demo.model.EventTypeProperties;
import primebot.demo.service.EventTypePropertiesService;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/eventType/properties")
public class EventTypePropertiesController {
    private final EventTypePropertiesService service;

    public EventTypePropertiesController(EventTypePropertiesService service) {
        this.service = service;
    }

    @PostMapping ("/create")
    public EventTypeProperties create(@RequestBody EventTypeProperties eventTypeProperties) {
        return service.create(eventTypeProperties);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<EventTypeProperties> update(
            @PathVariable Long id,
            @RequestBody EventTypeProperties eventTypeProperties) {

        return service.update(id, eventTypeProperties)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping ("/all")
    public List<EventTypeProperties> findAll() {
        return service.findAll();
    }


   @GetMapping ("/get/{id}")
    public Optional<EventTypeProperties> findById(@PathVariable Long id) {
        return service.findById(id);
    }






}
