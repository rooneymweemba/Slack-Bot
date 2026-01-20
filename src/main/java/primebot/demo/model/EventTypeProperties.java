package primebot.demo.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


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
    private LocalDateTime notifyAt;
    @Column(name = "schedule_type")
    private String scheduleType;

    @OneToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

}
