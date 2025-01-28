package com.desafio03.ms_ticket.model.dto;

import com.desafio03.ms_ticket.clientevents.EventResponseDto;

public record TicketRequestDto(
    String ticketId,
    String customerName,
    String cpf,
    String customerMail,
    String eventId,
    String eventName,
    String BRLtotalAmount,
    String USDtotalAmount
){
}

