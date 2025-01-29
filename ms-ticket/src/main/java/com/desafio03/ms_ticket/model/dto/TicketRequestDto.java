package com.desafio03.ms_ticket.model.dto;

public record TicketRequestDto(
    String ticketId,
    String customerName,
 // @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "Invalid CPF")
    String cpf,
    String customerMail,
    String eventId,
    String eventName,
    String BRLamount,
    String USDamount
){
}

