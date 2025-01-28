package com.desafio03.ms_ticket.service;

import com.desafio03.ms_ticket.clientevents.Event;
import com.desafio03.ms_ticket.clientevents.EventClient;
import com.desafio03.ms_ticket.model.Ticket;
import com.desafio03.ms_ticket.model.dto.TicketRequestDto;
import com.desafio03.ms_ticket.model.dto.TicketResponseDto;
import com.desafio03.ms_ticket.model.dto.mapper.TicketMapper;
import com.desafio03.ms_ticket.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private static final AtomicLong SEQUENCE_GENERATOR = new AtomicLong(1);

    private final TicketRepository ticketRepository;
    private final EventClient eventClient;

    public TicketResponseDto createTicket(TicketRequestDto dto) {
        log.info("Recebendo dados do evento para o ID: {}", dto.eventId());
        Event event = eventClient.getEventById(dto.eventId());
        log.info("Evento recebido: {}", event);

        Ticket ticket = TicketMapper.toTicket(dto, event);
        log.info("Ticket mapeado: {}", ticket);

        ticket.setTicketId(String.valueOf(SEQUENCE_GENERATOR.getAndIncrement()));
        ticketRepository.save(ticket);
        log.info("Ticket salvo no banco com ID: {}", ticket.getTicketId());

        return TicketMapper.toResponseDto(ticket);
    }
}
