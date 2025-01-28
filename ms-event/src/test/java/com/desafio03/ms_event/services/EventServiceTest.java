package com.desafio03.ms_event.services;

import com.desafio03.ms_event.clientviacep.Adress;
import com.desafio03.ms_event.clientviacep.ViacepClient;
import com.desafio03.ms_event.common.EventConstants;
import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.model.Event;
import com.desafio03.ms_event.repositories.EventRepository;
import com.desafio03.ms_event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ViacepClient viacepClient;

    private Event validEvent;

    private EventRequestDto validEventRequestDto;

    private Adress mockAddress;

    @BeforeEach
    public void setUp() {
        validEvent = EventConstants.VALID_EVENT;
        validEventRequestDto = EventConstants.VALID_EVENT_REQUEST_DTO;
        mockAddress = EventConstants.VALID_ADDRESS;
    }

    @Test
    public void createEvent_WithValidData_ReturnsCreatedEventWithStatus201() {
        when(eventRepository.save(validEvent)).thenReturn(validEvent);

        when(viacepClient.getAdress(validEvent.getCep())).thenReturn(mockAddress);

        EventResponseDto createdEvent = eventService.createEvent(validEventRequestDto);

        EventResponseDto createdEventResponse = EventConstants.VALID_EVENT_RESPONSE_DTO;

        assertThat(createdEvent).isNotNull();
        assertThat(createdEvent).isEqualTo(createdEventResponse);

        verify(viacepClient).getAdress(validEvent.getCep());
        verify(eventRepository).save(validEvent);
    }

}
