package com.desafio03.ms_event.service;

import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.model.Event;
import com.desafio03.ms_event.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;

    public EventResponseDto createEvent(EventRequestDto eventRequestDto) {
        Event event = Event.builder()
                .eventName(eventRequestDto.eventName())
                .dateTime(eventRequestDto.dateTime())
                .cep(eventRequestDto.cep())
                .build();
        eventRepository.save(event);
        log.info("Event created successfully");
        return new EventResponseDto(event.getId(), event.getEventName(), event.getDateTime(), event.getCep(), event.getPublicPlace(), event.getDistrict(), event.getCity(), event.getUf());
    }
}
