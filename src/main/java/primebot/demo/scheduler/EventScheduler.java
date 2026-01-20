package primebot.demo.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import primebot.demo.model.SlackEvent;
import primebot.demo.service.SlackEventService;
import primebot.demo.service.SlackMessageService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class EventScheduler {
    private final SlackEventService eventService;
    private final SlackMessageService messageService;

    public EventScheduler(SlackEventService eventService, SlackMessageService messageService) {
        this.eventService = eventService;
        this.messageService = messageService;

    }

    /*@Scheduled(fixedRate = 100_00)
    public void processScheduledEvents() {
        List<SlackEvent> dueEvents = eventService.getEventsForDate(LocalDate.now());
        for (SlackEvent event : dueEvents) {
            log.info("Processing event: {}", event.getName());

            if (!event.isNotified()) {
                boolean success = messageService.sendMessage(event.getChannelName(), event.getMessage());
                if (success) {
                    event.setNotified(true);
                    eventService.updateSlackEvent(event.getId());
                } else {
                    log.error("Failed to send message for event: {}", event.getName());
                }
            }
        }

    }*/
}
