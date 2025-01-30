package com.desafio03.ms_event.common;

import com.desafio03.ms_event.feign.viacep.Adress;
import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.model.Event;

import java.time.LocalDateTime;

public class EventConstants {

    public static final LocalDateTime TEST_DATE_TIME = LocalDateTime.of(2025, 1, 30, 10, 30);

    public static final Event VALID_EVENT = Event.builder()
            .eventName("Valid Event")
            .dateTime(TEST_DATE_TIME)
            .cep("01153-000")
            .logradouro("Rua Vitorino Carmilo")
            .bairro("Barra Funda")
            .cidade("São Paulo")
            .uf("SP")
            .build();

    public static final Event INVALID_EVENT = Event.builder()
            .eventName("Invalid Event")
            .dateTime(null)
            .cep("12345-678")
            .build();

    public static final EventRequestDto VALID_EVENT_REQUEST_DTO = new EventRequestDto(
            "Valid Event",
            TEST_DATE_TIME,
            "01153-000"
    );

    public static final EventResponseDto VALID_EVENT_RESPONSE_DTO = new EventResponseDto(
            VALID_EVENT.getId(),
            "Valid Event",
            TEST_DATE_TIME,
            "01153-000",
            "Rua Vitorino Carmilo",
            "Barra Funda",
            "São Paulo",
            "SP"
    );

    public static final Adress VALID_ADDRESS = new Adress(
            "01153-000",
            "Rua Vitorino Carmilo",
            "Barra Funda",
            "São Paulo",
            "SP"
    );
}
