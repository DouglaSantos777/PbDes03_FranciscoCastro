package com.desafio03.ms_ticket.model.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "id_sequence")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IdSequence {

    @Id
    private String id;

    private Long nextId;
}
