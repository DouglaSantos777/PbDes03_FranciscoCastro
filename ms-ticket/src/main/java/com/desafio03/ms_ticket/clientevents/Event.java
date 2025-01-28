package com.desafio03.ms_ticket.clientevents;

import java.time.LocalDateTime;

public record Event(
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
