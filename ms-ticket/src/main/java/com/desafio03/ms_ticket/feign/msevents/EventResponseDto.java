package com.desafio03.ms_ticket.feign.msevents;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventResponseDto(
        String eventId,
        String eventName,
        LocalDateTime eventDateTime,
        String logradouro,
        String bairro,
        String cidade,
        String uf
) {
}