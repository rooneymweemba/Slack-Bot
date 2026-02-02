package primebot.demo.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.*;


@Entity
@Table(name = "EVENT_TYPE_PROPERTIES")
@Getter
@Setter
public class EventTypeProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;
    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;
    @Column(name = "notify_at" , nullable = false)
    private LocalTime notifyAt;
    @Column(name = "schedule_type")
    private String scheduleType;
    @Column(name = "recurrence_pattern")
    private String recurrencePattern;
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    @Column(name = "month_day")
    private MonthDay monthDay;

}
