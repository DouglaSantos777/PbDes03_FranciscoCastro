package com.desafio03.ms_event.dto;

import java.time.LocalDateTime;

public record EventResponse(
         String id,
         String eventName,
         LocalDateTime dateTime,
         String cep,
         String publicPlace,
         String district,
         String city,
         String uf
) {
}
