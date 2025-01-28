package com.desafio03.ms_ticket.controller;

import com.desafio03.ms_ticket.clientevents.EventClient;
import com.desafio03.ms_ticket.clientevents.EventResponseDto;
import com.desafio03.ms_ticket.model.dto.TicketRequestDto;
import com.desafio03.ms_ticket.model.dto.TicketResponseDto;
import com.desafio03.ms_ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    private final EventClient eventClient;

    @PostMapping("create-ticket")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponseDto createTicket(@RequestBody TicketRequestDto dto) {

        EventResponseDto event = eventClient.getEventById(dto.eventId());

        return ticketService.createTicket(dto, event);
    }

}
