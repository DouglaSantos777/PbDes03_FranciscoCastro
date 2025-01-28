package com.desafio03.ms_ticket.model.dto;

public record TicketRequestDto(
    String ticketId,
    String customerName,
    String cpf,
    String customerMail,
    String eventId,
    String eventName,
    String BRLamount,
    String USDamount
){
}

