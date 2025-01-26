package com.desafio03.ms_event.service;

import com.desafio03.ms_event.clientviacep.ViacepClient;
import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.dto.mapper.EventMapper;
import com.desafio03.ms_event.model.Event;
import com.desafio03.ms_event.repositories.EventRepository;
import exception.EntityNotFoundException;
import exception.EventNameAlreadyExistsException;
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

    public Event createEvent(Event event) {
        boolean eventAlreadyExists = eventRepository.existsByEventName(event);

        if (eventAlreadyExists) {
            throw new EventNameAlreadyExistsException("Event Already exists with name " + event.getEventName() + " .");
        }

        if (event.getCep() != null) {
            var adress = viacepClient.getAdress(event.getCep());

            if (adress != null) {
                event.setLogradouro(adress.logradouro());
                event.setBairro(adress.bairro());
                event.setCidade(adress.localidade());
                event.setUf(adress.uf());
            }
        }

        eventRepository.save(event);
        log.info("Event created successfully");
        return event;
    }

    /*
    public Event getEvent(String id) {

        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));
    }

    public EventResponseDto createEvent(EventRequestDto eventRequestDto) {
        Event event = EventMapper.toEvent(eventRequestDto);
        boolean eventAlreadyExists = eventRepository.existsByEventName(event);

        if(eventAlreadyExists){
            throw new EventNameAlreadyExistsException("Event Already exists with name " + event.getEventName() + " .");
        }

        if (event.getCep() != null){
            var adress = viacepClient.getAdress(event.getCep());

            if (adress != null) {
                event.setLogradouro(adress.logradouro());
                event.setBairro(adress.bairro());
                event.setCidade(adress.localidade());
                event.setUf(adress.uf());
            }
        }

        eventRepository.save(event);
        log.info("Event created successfully");
        return EventMapper.toResponseDto(event);
    }
*/

    public EventResponseDto getEvent(String id) {

        return eventRepository.findById(id)
                .map(EventMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));
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
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        event.setEventName(eventRequestDto.eventName());
        event.setDateTime(eventRequestDto.dateTime());
        event.setCep(eventRequestDto.cep());

        eventRepository.save(event);
        log.info("Event updated successfully: {}", id);

        return EventMapper.toResponseDto(event);
    }

    public void deleteEvent(String id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + id));

        eventRepository.deleteById(event.getId());
        log.info("Event deleted successfully: {}", id);
    }
}
