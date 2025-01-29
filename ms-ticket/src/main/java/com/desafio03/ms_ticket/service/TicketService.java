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

    public TicketResponseDto getTicketById(String id) {
        return ticketRepository.findById(id)
                .map(TicketMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + id));
    }

    public TicketResponseDto getTicketByCpf(String cpf) {
        return ticketRepository.findByCpf(cpf)
                .map(TicketMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Ticket not found with cpf: " + cpf));
    }

    public TicketResponseDto updateTicket(String id, TicketRequestDto dto) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found with Id: " + id));

        Event event = eventClient.getEventById(dto.eventId());
        log.info("Event received: {}", event);

        if (dto.cpf() != null) ticket.setCpf(dto.cpf());
        if (dto.customerName() != null) ticket.setCustomerName(dto.customerName());
        if (dto.customerMail() != null) ticket.setCustomerMail(dto.customerMail());
        if (event != null) ticket.setEvent(event);
        if (dto.BRLamount() != null) ticket.setBRLtotalAmount(dto.BRLamount());
        if (dto.USDamount() != null) ticket.setUSDtotalAmount(dto.USDamount());

        ticketRepository.save(ticket);
        log.info("Ticket updated successfully: {}", id);

        return TicketMapper.toResponseDto(ticket);
    }

    public TicketResponseDto cancelTicketById(String id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + id));

        ticket.setStatus("cancelado");

        ticketRepository.save(ticket);
        log.info("Ticket canceled successfully: {}", id);
        return TicketMapper.toResponseDto(ticket);
    }
}
