package primebot.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "EVENT_TYPE")
@Getter
@Setter
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_type_properties_id", nullable = false)
    private EventTypeProperties properties;
    @OneToMany(mappedBy = "eventType", fetch = FetchType.LAZY)
    private List<SlackEvent> slackEvents;

}
