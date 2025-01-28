package com.desafio03.ms_ticket.model;

import com.desafio03.ms_ticket.clientevents.EventResponseDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tickets")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "ticketId")
public class Ticket {

    @Id
    private String ticketId;
    private String customerName;
    private String cpf;
    private String customerMail;
    private EventResponseDto event;
    private String BRLtotalAmount;
    private String USDtotalAmount;
    private String status;
}

