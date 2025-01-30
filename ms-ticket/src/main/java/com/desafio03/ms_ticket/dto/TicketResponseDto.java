package com.desafio03.ms_ticket.dto;

import com.desafio03.ms_ticket.feign.msevents.EventResponseDto;

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
