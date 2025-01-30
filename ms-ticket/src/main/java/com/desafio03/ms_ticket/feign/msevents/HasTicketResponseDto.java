package com.desafio03.ms_ticket.feign.msevents;

public record HasTicketResponseDto (
        String eventId,
        boolean hasTickets
) {
}

