package com.desafio03.ms_ticket.clientevents;

import java.time.LocalDateTime;

public record EventResponseDto(
        String eventId,
        String eventName,
        LocalDateTime eventDateTime,
        String cep,
        String logradouro,
        String bairro,
        String cidade,
        String uf
) {
}