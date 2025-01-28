package com.desafio03.ms_ticket.model.dto;

import com.desafio03.ms_ticket.clientevents.EventResponseDto;

public record TicketResponseDto(
        String ticketId,
        String cpf,
        String customerName,
        String customerMail,
        EventResponseDto event,
        String BRLtotalAmount,
        String USDtotalAmount,
        String status

){
}
