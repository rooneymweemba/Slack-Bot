package primebot.demo.scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import primebot.demo.model.EventType;
import primebot.demo.model.EventTypeProperties;
import primebot.demo.model.SlackEvent;
import primebot.demo.repository.EventTypeRepository;
import primebot.demo.repository.SlackEventRepository;
import primebot.demo.service.SlackMessageService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Slf4j

@Component
public class EventScheduler {

    private final EventTypeRepository eventTypeRepository;
    private final SlackMessageService slackMessageService;
    private final SlackEventRepository slackEventRepository;
    public EventScheduler(
            EventTypeRepository eventTypeRepository,
            SlackMessageService slackMessageService, SlackEventRepository slackEventRepository) {
        this.eventTypeRepository = eventTypeRepository;
        this.slackMessageService = slackMessageService;
        this.slackEventRepository = slackEventRepository;
    }

    @Scheduled(fixedRate = 60_00) // every minute
    public void run() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        log.info("Scheduler tick: {}", now);

        List<EventType> eventTypes = eventTypeRepository.findAllWithPropertiesAndEvents();

        for (EventType eventType : eventTypes) {
            processEventType(eventType, now);
            log.info("Processed event type: {} at {}", eventType.getName(), now);
        }
    }
    private void processEventType(EventType eventType, LocalDateTime now) {
        EventTypeProperties props = eventType.getProperties();

        if (props == null){
            log.info("No properties for event type: {}", eventType.getName());
            return;}

        if (!shouldTrigger(props, now)){
            log.info("No trigger for event type: {} at {}", eventType.getName(), now);
            return;}

        for (SlackEvent event : eventType.getSlackEvents()) {
            sendSlackNotification(event);
            log.info("Triggered event: {} at {}", event.getName(), now);
        }
    }
    private boolean shouldTrigger(EventTypeProperties p, LocalDateTime now) {


        if (!now.toLocalTime().equals(p.getNotifyAt())) {
            log.info("Current time {} does not match notify time {}", now.toLocalTime(), p.getNotifyAt());
            return false;
        }

        if (Objects.equals(p.getScheduleType(), "ONE_TIME")) {
            log.info("One-time event does not match for time: {}", now);
            return matchesOneTime(p, now);

        }

        if (Objects.equals(p.getScheduleType(), "RECURRING")) {
            log.info("Checking recurring event for time: {}", now);
            return matchesRecurring(p, now);
        }

        return false;
    }
    private boolean matchesOneTime(EventTypeProperties p, LocalDateTime now) {
        LocalDateTime triggerTime =
                LocalDateTime.of(p.getStartDateTime().toLocalDate(), p.getNotifyAt());
        log.info("One-time event trigger time: {}", triggerTime);
        return now.equals(triggerTime);
    }
    private boolean matchesRecurring(EventTypeProperties p, LocalDateTime now) {

        LocalDate today = now.toLocalDate();
        log.info("Recurring event check for date: {}", today);

        if (!isWithinDateRange(p, today)) {
            log.info("Date {} is outside the event date range", today);
            return false;
        }

        return switch (p.getRecurrencePattern()) {
            case "DAILY" -> true;
            case "WEEKLY" -> today.getDayOfWeek() == p.getDayOfWeek();
            case "YEARLY" -> MonthDay.from(today).equals(p.getMonthDay());
            default -> false;
        };
    }
    private boolean isWithinDateRange(EventTypeProperties p, LocalDate today) {

        if (p.getStartDateTime() != null && today.isBefore(p.getStartDateTime().toLocalDate())) {
            return false;
        }

        if (p.getEndDateTime() != null && today.isAfter(p.getEndDateTime().toLocalDate())) {
            return false;
        }

        return true;
    }
    private void sendSlackNotification(SlackEvent event) {
        log.info("Sending Slack alert for event: {}", event.getName());


        if (event.getLastTriggeredAt() != null &&
                event.getLastTriggeredAt().truncatedTo(ChronoUnit.MINUTES)
                        .equals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {
             log.info("Slack message already sent for event {} at {}", event.getId(), event.getLastTriggeredAt());
            return;
        }
        boolean sent = slackMessageService.sendMessage(
                event.getChannelName(),
                event.getMessage()
        );
        if (sent){
            event.setLastTriggeredAt(LocalDateTime.now());
            slackEventRepository.save(event);
        }
        if (!sent) {
            log.error("Slack message failed for event {}", event.getId());
        }

    }


}