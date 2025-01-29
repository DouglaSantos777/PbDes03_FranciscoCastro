package com.desafio03.ms_ticket.clientevents;

public record HasTicketResponseDto (
        String eventId,
        boolean hasTickets
) {
}

