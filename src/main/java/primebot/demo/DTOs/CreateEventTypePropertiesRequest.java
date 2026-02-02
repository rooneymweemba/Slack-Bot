package primebot.demo.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateEventTypePropertiesRequest {
    private Long eventTypeId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime notifyAt;
    private String scheduleType;


}
