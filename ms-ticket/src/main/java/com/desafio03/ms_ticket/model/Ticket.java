package com.desafio03.ms_ticket.model;

import com.desafio03.ms_ticket.feign.msevents.Event;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

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

    @Override
    public String toString() {
        return "Id do ingresso: " + ticketId + "\n" +
                "Cliente: " + customerName + "\n" +
                "CPF: " + cpf + "\n" +
                "Email do Cliente: " + customerMail + "\n" +
                "Evento: " + event.eventName() + "\n" +
                "Data: " + event.dateTime().toLocalDate() + "\n" +
                "Hora: " + event.dateTime().toLocalTime() + "\n" +
                "Local: " + event.logradouro() + ", " + event.bairro() + ", " + event.cidade() + ", " + event.uf() + ", " + event.cep() + "\n" +
                "Valor Total (BRL): " + BRLtotalAmount + "\n" +
                "Valor Total (USD): " + USDtotalAmount + "\n" +
                "Status: " + status;
    }
}

