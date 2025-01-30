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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    private EventRequestDto validEvent;

    @Autowired
    private ObjectMapper objectMapper;


    private EventResponseDto validEventResponse;

    @BeforeEach
    public void setUp() {
        validEvent = EventConstants.VALID_EVENT_REQUEST_DTO;
        validEventResponse = EventConstants.VALID_EVENT_RESPONSE_DTO;
    }

    @Test
    public void createEvent_WithValidData_ReturnsCreatedEvent() throws Exception {
        when(eventService.createEvent(any(EventRequestDto.class))).thenReturn(validEventResponse);

        mockMvc.perform(post("/api/v1/events/create-event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventName").value(validEventResponse.eventName()));

        verify(eventService).createEvent(any(EventRequestDto.class));
    }

    }

