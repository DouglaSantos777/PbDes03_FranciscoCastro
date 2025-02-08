package com.desafio03.ms_event.controller;

import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.service.EventService;
import com.desafio03.ms_event.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Event Manager", description = "APIs for managing events")
@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @Operation(summary = "Create a new event",
            description = "This endpoint allows to create a new event.",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Event successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class))),
                    @ApiResponse(responseCode = "409",
                            description = "Event name already exists",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422",
                            description = "Invalid data provided",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("create-event")
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto createEvent(@Valid @RequestBody EventRequestDto eventRequestDto) {

        return eventService.createEvent(eventRequestDto);
    }

    @Operation(summary = "Get event by ID",
            description = "This endpoint returns an event by its unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Event found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Event not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("get-event/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponseDto getEvent(@PathVariable String id) {
        return eventService.getEvent(id);
    }

    @Operation(summary = "Get all events",
            description = "This endpoint returns a list of all events.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "List of events retrieved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class)))
            })
    @GetMapping("get-all-events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponseDto> getAllEvents() {
        return eventService.getAllEvents();
    }

    @Operation(summary = "Get all events sorted alphabetically",
            description = "This endpoint returns a list of all events sorted by date or another criterion.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "List of sorted events retrieved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class)))
            })
    @GetMapping("get-all-events/sorted")
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponseDto> getAllEventsSorted() {
        return eventService.getAllEventsSorted();
    }

    @Operation(summary = "Update event",
            description = "This endpoint allows to update an existing event by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Event successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Event not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping("update-event/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponseDto updateEvent(@PathVariable String id, @Valid @RequestBody EventRequestDto eventRequestDto) {
        return eventService.updateEvent(id, eventRequestDto);
    }

    @Operation(summary = "Delete event",
            description = "This endpoint allows to delete an event by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Event successfully deleted")
            })
    @DeleteMapping("delete-event/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

}
