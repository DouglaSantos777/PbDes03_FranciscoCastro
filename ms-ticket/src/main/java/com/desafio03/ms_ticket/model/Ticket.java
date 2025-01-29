package com.desafio03.ms_ticket.model;

import com.desafio03.ms_ticket.clientevents.Event;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private Event event;
    private BigDecimal BRLtotalAmount;
    private BigDecimal USDtotalAmount;
    private String status;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}

