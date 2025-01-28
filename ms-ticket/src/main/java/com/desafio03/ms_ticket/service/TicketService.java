package com.desafio03.ms_ticket.service;

import com.desafio03.ms_ticket.clientevents.EventClient;
import com.desafio03.ms_ticket.clientevents.EventResponseDto;
import com.desafio03.ms_ticket.model.Ticket;
import com.desafio03.ms_ticket.model.dto.TicketRequestDto;
import com.desafio03.ms_ticket.model.dto.TicketResponseDto;
import com.desafio03.ms_ticket.model.dto.mapper.TicketMapper;
import com.desafio03.ms_ticket.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventClient eventClient;

    public TicketResponseDto createTicket(TicketRequestDto dto, EventResponseDto event) {
        Ticket ticket = TicketMapper.toTicket(dto, event);

        ticketRepository.save(ticket);

        return TicketMapper.toResponseDto(ticket);
    }
}
