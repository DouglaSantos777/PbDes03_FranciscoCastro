package com.desafio03.ms_ticket.controller;

import com.desafio03.ms_ticket.feign.msevents.HasTicketResponseDto;
import com.desafio03.ms_ticket.dto.TicketRequestDto;
import com.desafio03.ms_ticket.dto.TicketResponseDto;
import com.desafio03.ms_ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("create-ticket")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponseDto createTicket(@RequestBody TicketRequestDto dto) {
        return ticketService.createTicket(dto);
    }

    @GetMapping("get-ticket/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponseDto getTicketById(@PathVariable String id) {
        return ticketService.getTicketById(id);
    }

    @GetMapping("get-ticket-by-cpf/{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponseDto getTicketByCpf(@PathVariable String cpf) {
        return ticketService.getTicketByCpf(cpf);
    }

    @GetMapping("check-tickets-by-event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public HasTicketResponseDto checkTicketsByEventId(@PathVariable String eventId) {
        return ticketService.checkTicketsByEventsId(eventId);
    }

    @PutMapping("update-ticket/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponseDto updateTicket(@PathVariable String id, @RequestBody TicketRequestDto dto) {
        return ticketService.updateTicket(id, dto);
    }

    @DeleteMapping("cancel-ticket/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponseDto cancelTicketById(@PathVariable String id) {
        return ticketService.cancelTicketById(id);
    }

    @DeleteMapping("cancel-ticket-by-cpf/{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponseDto cancelTicketByCpf(@PathVariable String cpf) {
        return ticketService.cancelTicketByCpf(cpf);
    }
}
