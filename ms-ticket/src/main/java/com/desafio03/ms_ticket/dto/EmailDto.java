package com.desafio03.ms_ticket.dto;

public record EmailDto(String ticketId,
                       String emailTo,
                       String subject,
                       String text) {
}
