package primebot.demo.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateEventTypeRequest {

private String name;
private String description;
private Long eventTypePropertyId;


}
