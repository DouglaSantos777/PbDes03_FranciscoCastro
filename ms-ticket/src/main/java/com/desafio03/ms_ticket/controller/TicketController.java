package com.desafio03.ms_ticket.controller;

import com.desafio03.ms_ticket.clientevents.EventClient;
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
