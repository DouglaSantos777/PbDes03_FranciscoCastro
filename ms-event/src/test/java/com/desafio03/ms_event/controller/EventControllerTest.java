package com.desafio03.ms_event.controller;

import com.desafio03.ms_event.common.EventConstants;
import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    private EventRequestDto validEvent;

    private EventRequestDto invalidEvent;

    private EventResponseDto validEventResponse;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() {
        validEvent = EventConstants.VALID_EVENT_REQUEST_DTO;
        validEventResponse = EventConstants.VALID_EVENT_RESPONSE_DTO;
        invalidEvent = EventConstants.INVALID_EVENT_REQUEST_DTO;
    }

    @Test
    public void createEvent_WithValidData_ReturnsCreatedEventWithStatus201() throws Exception {
        when(eventService.createEvent(any(EventRequestDto.class))).thenReturn(validEventResponse);

        mockMvc.perform(post("/api/v1/events/create-event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventName").value(validEventResponse.eventName()));

        verify(eventService).createEvent(any(EventRequestDto.class));
    }

    @Test
    public void createEvent_WithInvalidData_ReturnsStatus422() throws Exception {
        mockMvc.perform(post("/api/v1/events/create-event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEvent)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").exists());

        verify(eventService, never()).createEvent(any(EventRequestDto.class));
    }

    @Test
    public void getEvent_ByExistingId_ReturnsEventWithStatus200() throws Exception {
        when(eventService.getEvent(anyString())).thenReturn(validEventResponse);

        mockMvc.perform(get("/api/v1/events/get-event/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName").value(validEventResponse.eventName()));

        verify(eventService).getEvent(anyString());
    }

    @Test
    public void getAllEvents_ReturnsListOfEventsWithStatus200() throws Exception {
        when(eventService.getAllEvents()).thenReturn(List.of(validEventResponse));

        mockMvc.perform(get("/api/v1/events/get-all-events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName").value(validEventResponse.eventName()));

        verify(eventService).getAllEvents();
    }

    @Test
    public void getAllEventsSorted_ReturnsSortedListOfEventsWithStatus200() throws Exception {
        when(eventService.getAllEventsSorted()).thenReturn(List.of(validEventResponse));

        mockMvc.perform(get("/api/v1/events/get-all-events/sorted")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName").value(validEventResponse.eventName()));

        verify(eventService).getAllEventsSorted();
    }

    @Test
    public void updateEvent_WithValidData_ReturnsUpdatedEventWithStatus200() throws Exception {
        when(eventService.updateEvent(anyString(), any(EventRequestDto.class))).thenReturn(validEventResponse);

        mockMvc.perform(put("/api/v1/events/update-event/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validEvent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName").value(validEventResponse.eventName()));

        verify(eventService).updateEvent(anyString(), any(EventRequestDto.class));
    }

    @Test
    public void deleteEvent_WhenEventExists_ReturnsNoContentWithStatus204() throws Exception {
        doNothing().when(eventService).deleteEvent(anyString());

        mockMvc.perform(delete("/api/v1/events/delete-event/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(eventService).deleteEvent(anyString());
    }

}

