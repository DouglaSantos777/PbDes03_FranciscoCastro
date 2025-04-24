package com.desafio03.ms_ticket.common;

import com.desafio03.ms_ticket.dto.TicketRequestDto;
import com.desafio03.ms_ticket.dto.TicketResponseDto;
import com.desafio03.ms_ticket.feign.msevents.Event;
import com.desafio03.ms_ticket.feign.msevents.EventResponseDto;
import com.desafio03.ms_ticket.model.Ticket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketConstants {

    public static final LocalDateTime TEST_DATE_TIME = LocalDateTime.of(2026, 1, 30, 10, 30);

    public static final Event VALID_EVENT = new Event(
            "abcdefghijk",
            "Valid Event",
            TEST_DATE_TIME,
            "01153-000",
            "Rua Vitorino Carmilo",
            "Barra Funda",
            "São Paulo",
            "SP"
    );

    public static final EventResponseDto VALID_EVENT_RESPONSE_DTO = EventResponseDto.builder()
            .eventId("abcdefghijk")
            .eventName("Valid Event")
            .eventDateTime(TEST_DATE_TIME)
            .logradouro("Rua Vitorino Carmilo")
            .bairro("Barra Funda")
            .cidade("São Paulo")
            .uf("SP")
            .build();

    public static final Ticket VALID_TICKET = Ticket.builder()
            .ticketId("1")
            .customerName("Valid User")
            .cpf("123.456.789-00")
            .customerMail("validmail@gmail.com")
            .event(VALID_EVENT)
            .BRLtotalAmount(new BigDecimal("150.50"))
            .USDtotalAmount(new BigDecimal("30.10"))
            .status("concluído")
            .build();

    public static final TicketRequestDto VALID_TICKET_REQUEST_DTO = new TicketRequestDto(
            "Valid User",
            "123.456.789-00",
            "validmail@gmail.com",
            "abcdefghijk",
            "Valid Event",
            "R$ 150,50",
            "$ 30,10"
    );

    public static final TicketRequestDto INVALID_TICKET_REQUEST_DTO = new TicketRequestDto(
            "Invalid User",
            "123.456.789",
            "validmail@gmail.com",
            "abcdefghijk",
            "Valid Event",
            "R$ 150,50",
            "$ 30,10"
    );

    public static final TicketResponseDto VALID_TICKET_RESPONSE_DTO = new TicketResponseDto(
            "1",
            "123.456.789-00",
            "Valid User",
            "validmail@gmail.com",
            VALID_EVENT_RESPONSE_DTO,
            "R$ 150,50",
            "$ 30,10",
            "concluído"
    );

}
