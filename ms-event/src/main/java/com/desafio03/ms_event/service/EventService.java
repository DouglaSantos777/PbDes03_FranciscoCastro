package com.desafio03.ms_event.service;

import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.dto.mapper.EventMapper;
import com.desafio03.ms_event.model.Event;
import com.desafio03.ms_event.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;

    public EventResponseDto createEvent(EventRequestDto eventRequestDto) {
        Event event = EventMapper.toEvent(eventRequestDto);
        eventRepository.save(event);
        log.info("Event created successfully");
        return EventMapper.toResponseDto(event);
    }

    public List<EventResponseDto> getAllEvents(){
        return eventRepository.findAll()
                .stream()
                .map(EventMapper::toResponseDto)
                .toList();
    }

    public EventResponseDto getEvent(String id){
       return eventRepository.findById(id)
               .map(EventMapper::toResponseDto)
               .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

    }

    public EventResponseDto updateEvent(String id, EventRequestDto eventRequestDto){
    Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        event.setEventName(eventRequestDto.eventName());
        event.setDateTime(eventRequestDto.dateTime());
        event.setCep(eventRequestDto.cep());

        eventRepository.save(event);
        log.info("Event updated successfully: {}", id);

        return EventMapper.toResponseDto(event);
    }
}
