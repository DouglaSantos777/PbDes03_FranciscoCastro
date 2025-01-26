package com.desafio03.ms_event.controller;

import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.dto.mapper.EventMapper;
import com.desafio03.ms_event.model.Event;
import com.desafio03.ms_event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("create-event")
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto createEvent(@RequestBody EventRequestDto eventRequestDto) {
        Event event = EventMapper.toEvent(eventRequestDto);

        return EventMapper.toResponseDto(eventService.createEvent(event));
    }

    @GetMapping("get-event/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponseDto getEvent(@PathVariable String id) {
        return EventMapper.toResponseDto(eventService.getEvent(id));
    }

    @GetMapping("get-all-events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponseDto> getAllEvents() {

        return eventService.getAllEvents()
                .stream()
                .map(EventMapper::toResponseDto)
                .toList();
    }

    @GetMapping("get-all-events/sorted")
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponseDto> getAllEventsSorted() {

        return eventService.getAllEventsSorted()
                .stream()
                .map(EventMapper::toResponseDto)
                .toList();
    }

    @PutMapping("update-event/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponseDto updateEvent(@PathVariable String id, @RequestBody EventRequestDto eventRequestDto) {
        Event event = EventMapper.toEvent(eventRequestDto);
        return EventMapper.toResponseDto(eventService.updateEvent(id, event));
    }

    @DeleteMapping("delete-event/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

}
