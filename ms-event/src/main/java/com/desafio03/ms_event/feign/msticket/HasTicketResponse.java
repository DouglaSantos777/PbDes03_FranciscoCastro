package com.desafio03.ms_event.feign.msticket;

public record HasTicketResponse(String eventId, boolean hasTickets) {
}