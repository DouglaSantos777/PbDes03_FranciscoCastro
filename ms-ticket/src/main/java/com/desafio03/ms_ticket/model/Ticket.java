package com.desafio03.ms_ticket.model;

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
    private String cpf;
    private String customerName;
    private String customerMail;
    private String event;
    private String BRLtotalAmount;
    private String USDtotalAmount;
    private String status;
}

