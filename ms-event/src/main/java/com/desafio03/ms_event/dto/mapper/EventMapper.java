package com.desafio03.ms_event.dto.mapper;

import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.model.Event;

public class EventMapper {

    public static Event toEvent(EventRequestDto dto){
        return Event.builder()
                .eventName(dto.eventName())
                .dateTime(dto.dateTime())
                .cep(dto.cep())
                .build();
    }

    public static EventResponseDto toResponseDto(Event event) {
        return new EventResponseDto(
                event.getId(),
                event.getEventName(),
                event.getDateTime(),
                event.getCep(),
                event.getPublicPlace(),
                event.getDistrict(),
                event.getCity(),
                event.getUf()
        );
    }
}
