package com.desafio03.ms_event.service;

import com.desafio03.ms_event.exception.EventConflictException;
import com.desafio03.ms_event.feign.msticket.HasTicketResponse;
import com.desafio03.ms_event.feign.msticket.MsTicketClient;
import com.desafio03.ms_event.feign.viacep.ViacepClient;
import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.dto.mapper.EventMapper;
import com.desafio03.ms_event.model.Event;
import com.desafio03.ms_event.repository.EventRepository;
import com.desafio03.ms_event.exception.EventNotFoundException;
import com.desafio03.ms_event.exception.EventWithTicketsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final ViacepClient viacepClient;
    private final MsTicketClient msTicketClient;

    public EventResponseDto createEvent(EventRequestDto eventRequestDto) {
        checkLocalAndDateAvailability(eventRequestDto);

        Event event = EventMapper.toEvent(eventRequestDto);
        setEventAddress(event);

        eventRepository.save(event);
        log.info("Event created successfully");
        return EventMapper.toResponseDto(event);
    }

    public EventResponseDto getEvent(String id) {
        return eventRepository.findById(id)
                .map(EventMapper::toResponseDto)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));
    }

    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(EventMapper::toResponseDto)
                .toList();
    }


    public List<EventResponseDto> getAllEventsSorted() {
        return eventRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Event::getEventName))
                .map(EventMapper::toResponseDto)
                .toList();
    }

    public EventResponseDto updateEvent(String id, EventRequestDto eventRequestDto) {
        checkLocalAndDateAvailability(eventRequestDto);

        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));

        event.setEventName(eventRequestDto.eventName());
        event.setDateTime(eventRequestDto.dateTime());
        event.setCep(eventRequestDto.cep());

        eventRepository.save(event);
        log.info("Event updated successfully: {}", id);

        return EventMapper.toResponseDto(event);
    }


    public void deleteEvent(String id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));

        HasTicketResponse eventSituation = msTicketClient.checkTicketsByEvent(id);
        if (eventSituation.hasTickets()) {
            throw new EventWithTicketsException("The event with ID " + event.getId() + " can't be deleted because it has sold tickets.");
        }

        eventRepository.deleteById(event.getId());
        log.info("Event deleted successfully: {}", id);
    }

    private void checkLocalAndDateAvailability(EventRequestDto eventRequestDto) {
        boolean isAvailable = eventRepository.findAll().stream()
                .anyMatch(event -> event.getCep().equalsIgnoreCase(eventRequestDto.cep()) &&
                        event.getDateTime().equals(eventRequestDto.dateTime()));

        if (isAvailable) {
            throw new EventConflictException("The location at CEP " + eventRequestDto.cep()
                    + " is not available on the date "
                    + eventRequestDto.dateTime());
        }
    }

    private void setEventAddress(Event event) {
        if (event.getCep() != null) {
            var address = viacepClient.getAdress(event.getCep());

            if (address != null) {
                event.setLogradouro(address.logradouro());
                event.setBairro(address.bairro());
                event.setCidade(address.localidade());
                event.setUf(address.uf());
            }
        }
    }

}
