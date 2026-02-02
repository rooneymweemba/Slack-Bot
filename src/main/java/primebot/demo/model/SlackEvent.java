package primebot.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "slack_event")
@Getter
@Setter
public class SlackEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String message;
    private String createdBy;

    private String channelName;
    private String channelKey;

    private boolean notified;
    private LocalDateTime lastTriggeredAt;

    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;
}
