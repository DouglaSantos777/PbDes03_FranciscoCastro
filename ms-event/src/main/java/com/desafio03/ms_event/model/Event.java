package com.desafio03.ms_event.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "tb_events")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Event {
    @Id
    private String id;
    private String eventName;
    private LocalDateTime dateTime;
    private String cep;
    private String publicPlace;
    private String district;
    private String city;
    private String uf;

}
