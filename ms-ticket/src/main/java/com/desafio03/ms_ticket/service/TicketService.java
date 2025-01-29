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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventClient eventClient;
    private final IdSequenceService idSequenceService;

    public TicketResponseDto createTicket(TicketRequestDto dto) {
        Event event = null;

        try {
            event = eventClient.getEventById(dto.eventId());
        } catch (Exception e) {
            log.info("Event not found with id: {}", dto.eventId());
        }

        if (event == null) {
            event = eventClient.getAllEvents()
                    .stream()
                    .filter(e -> e.eventName().equalsIgnoreCase(dto.eventName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Event not found with id: " + dto.eventId() + " or name: " + dto.eventName()));
        }

        Ticket ticket = TicketMapper.toTicket(dto, event);

        ticket.setTicketId(String.valueOf(idSequenceService.getNextId()));

        ticketRepository.save(ticket);

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

    public TicketResponseDto cancelTicketByCpf(String cpf) {
        Ticket ticket = ticketRepository.findByCpf(cpf).orElseThrow(() -> new RuntimeException("Ticket not found with cpf: " + cpf));

        ticket.setStatus("cancelado");

        ticketRepository.save(ticket);
        log.info("Ticket canceled by cpf successfully: {}", cpf);
        return TicketMapper.toResponseDto(ticket);
    }

    public List<TicketResponseDto> checkTicketsByEventsId(String eventId) {
        return ticketRepository.findByEventId(eventId)
                .stream()
                .map(TicketMapper::toResponseDto)
                .toList();
    }

}
