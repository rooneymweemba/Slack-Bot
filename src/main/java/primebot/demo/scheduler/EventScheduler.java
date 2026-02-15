package primebot.demo.scheduler;
import jakarta.annotation.PostConstruct;
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

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Slf4j

@Component
public class EventScheduler {
    @PostConstruct
    public void init() {
        System.out.print("EventScheduler initialized: {}" + this);
    }

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

    @Scheduled(fixedRate = 60_00)
    public void run() {

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        log.info("Scheduler tick: {}", now);

        try {
            List<EventType> eventTypes = eventTypeRepository.findAllWithPropertiesAndEvents();

            for (EventType eventType : eventTypes) {
                log.info("looping event type: {}", eventType.getName());
                processEventType(eventType, now);

            }
        } catch (Exception e) {
            log.error("Error processing scheduled events", e);
        }
    }
    private void processEventType(EventType eventType, LocalDateTime now) {
        EventTypeProperties props = eventType.getProperties();

        if (props == null){

            return;}

        if (!shouldTrigger(props, now)){

            return;}

        try {
            for (SlackEvent event : eventType.getSlackEvents()) {
                log.info("Triggered event: {} at {}", event.getName(), now);
                sendSlackNotification(event);

            }
        } catch (Exception e) {
            log.error("Error processing event type: {}", eventType.getName(), e);
        }
    }
    private boolean shouldTrigger(EventTypeProperties p, LocalDateTime now) {


        if (!now.toLocalTime().equals(p.getNotifyAt())) {
           ;
            return false;
        }

        if (Objects.equals(p.getScheduleType(), "ONE_TIME")) {

            return matchesOneTime(p, now);

        }

        if (Objects.equals(p.getScheduleType(), "RECURRING")) {
            log.info("Checking recurring event at {}", p.getId());
            return matchesRecurring(p, now);
        }

        return false;
    }
    private boolean matchesOneTime(EventTypeProperties p, LocalDateTime now) {
        LocalDateTime triggerTime =
                LocalDateTime.of(p.getStartDateTime().toLocalDate(), p.getNotifyAt());

        return now.equals(triggerTime);
    }
    private boolean matchesRecurring(EventTypeProperties p, LocalDateTime now) {

        LocalDate today = now.toLocalDate();


        if (!isWithinDateRange(p, today)) {
            return false;
        }

        return switch (p.getRecurrencePattern()) {
            case "DAILY" -> true;
            case "WEEKLY" -> today.getDayOfWeek() == p.getDayOfWeek();
            case "MONTHLY" ->{
                    log.info("Checking monthly event for day {}", p.getMonthDay());
                     yield today.getDayOfMonth() == (p.getMonthDay());
            }
            default -> false;
        };
    }
    private boolean isWithinDateRange(EventTypeProperties p, LocalDate today) {

        if (p.getStartDateTime() != null && today.isBefore(p.getStartDateTime().toLocalDate())) {
            log.info("Date {} is before start date {}", today, p.getStartDateTime().toLocalDate());
            return false;
        }

        if (p.getEndDateTime() != null && today.isAfter(p.getEndDateTime().toLocalDate())) {
            log.info("Date {} is after end date {}", today, p.getEndDateTime().toLocalDate());
            return false;

        }

        return true;
    }
    private void sendSlackNotification(SlackEvent event) {


        try {
            if (event.getLastTriggeredAt() != null &&
                    event.getLastTriggeredAt().truncatedTo(ChronoUnit.MINUTES)
                            .equals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {

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
        } catch (Exception e) {
            log.error("Error sending Slack notification for event {}", event.getId(), e);
        }

    }


}
