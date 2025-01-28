package com.desafio03.ms_ticket.model.dto;

import java.time.LocalDateTime;

public record EventResponseDto(
        String id,
        String eventName,
        LocalDateTime dateTime,
        String cep,
        String logradouro,
        String bairro,
        String cidade,
        String uf
) {
}