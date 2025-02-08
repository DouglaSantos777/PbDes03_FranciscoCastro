package com.desafio03.ms_ticket.service;

import com.desafio03.ms_ticket.dto.TicketRequestDto;
import com.desafio03.ms_ticket.dto.TicketResponseDto;
import com.desafio03.ms_ticket.dto.mapper.TicketMapper;
import com.desafio03.ms_ticket.exception.CpfMismatchException;
import com.desafio03.ms_ticket.exception.EmailMismatchException;
import com.desafio03.ms_ticket.exception.EventNotFoundException;
import com.desafio03.ms_ticket.exception.TicketNotFoundException;
import com.desafio03.ms_ticket.feign.msevents.Event;
import com.desafio03.ms_ticket.feign.msevents.EventClient;
import com.desafio03.ms_ticket.feign.msevents.HasTicketResponseDto;
import com.desafio03.ms_ticket.model.Ticket;
import com.desafio03.ms_ticket.producer.TicketProducer;
import com.desafio03.ms_ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventClient eventClient;
    private final IdSequenceService idSequenceService;
    final TicketProducer ticketProducer;

    public TicketResponseDto createTicket(TicketRequestDto dto) {
        Event event = null;

        event = eventClient.getEventById(dto.eventId());


        if (event == null) {
            event = eventClient.getAllEvents()
                    .stream()
                    .filter(e -> e.eventName().equalsIgnoreCase(dto.eventName()))
                    .findFirst()
                    .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + dto.eventId() + " or name: " + dto.eventName()));
        }

        List<Ticket> existingCpfTicket = ticketRepository.findByCpf(dto.cpf());


        if (!existingCpfTicket.isEmpty() && existingCpfTicket.stream().noneMatch(ticket -> ticket.getCustomerName().equals(dto.customerName()))) {
            throw new CpfMismatchException("This CPF is already registered under a different costumer.");
        }

        Optional<Ticket> existingEmailTicket = ticketRepository.findByCustomerMail(dto.customerMail());

        if (existingEmailTicket.isPresent()) {
            Ticket ticket = existingEmailTicket.get();
            if (!ticket.getCpf().equals(dto.cpf())) {
                throw new EmailMismatchException("This email is already associated with a different costumer.");
            }
        }

        Ticket ticket = TicketMapper.toTicket(dto, event);
        ticket.setTicketId(String.valueOf(idSequenceService.getNextId()));
        ticketRepository.save(ticket);

        ticketProducer.publishEmail(ticket);

        return TicketMapper.toResponseDto(ticket);
    }

    public TicketResponseDto getTicketById(String id) {
        return ticketRepository.findById(id)
                .map(TicketMapper::toResponseDto)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + id));
    }

    public List<TicketResponseDto> getTicketByCpf(String cpf) {
        return ticketRepository.findByCpf(cpf)
                .stream()
                .map(TicketMapper::toResponseDto)
                .toList();
    }

    public TicketResponseDto updateTicket(String id, TicketRequestDto dto) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException("Ticket not found with Id: " + id));

        if (dto.cpf() != null) ticket.setCpf(dto.cpf());
        if (dto.customerName() != null) ticket.setCustomerName(dto.customerName());
        if (dto.customerMail() != null) ticket.setCustomerMail(dto.customerMail());

        ticketRepository.save(ticket);

        return TicketMapper.toResponseDto(ticket);
    }

    public TicketResponseDto cancelTicketById(String id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + id));

        ticket.setStatus("cancelado");

        ticketRepository.save(ticket);
        return TicketMapper.toResponseDto(ticket);
    }

    public List<TicketResponseDto> cancelTicketByCpf(String cpf) {
        List<Ticket> tickets = ticketRepository.findByCpf(cpf);
        if (tickets.isEmpty()) {
            throw new TicketNotFoundException("Ticket not found with cpf: " + cpf);
        }

        tickets.forEach(ticket -> ticket.setStatus("cancelado"));

        return ticketRepository.saveAll(tickets)
                .stream()
                .map(TicketMapper::toResponseDto)
                .toList();
    }

    public HasTicketResponseDto checkTicketsByEventsId(String eventId) {
        boolean hasTickets = ticketRepository.existsByEventId(eventId);
        return new HasTicketResponseDto(eventId, hasTickets);
    }
}
