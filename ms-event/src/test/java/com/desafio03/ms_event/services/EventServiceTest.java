package com.desafio03.ms_event.services;

import com.desafio03.ms_event.clientviacep.Adress;
import com.desafio03.ms_event.clientviacep.ViacepClient;
import com.desafio03.ms_event.common.EventConstants;
import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.model.Event;
import com.desafio03.ms_event.repositories.EventRepository;
import com.desafio03.ms_event.service.EventService;
import com.desafio03.ms_event.exception.EventNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.desafio03.ms_event.common.EventConstants.TEST_DATE_TIME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

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

    private LocalDateTime testDateTime;

    @BeforeEach
    public void setUp() {
        validEvent = EventConstants.VALID_EVENT;
        validEventRequestDto = EventConstants.VALID_EVENT_REQUEST_DTO;
        mockAddress = EventConstants.VALID_ADDRESS;
        testDateTime = EventConstants.TEST_DATE_TIME;
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

    @Test
    public void createEvent_WhenCepNotFound_DoesNotSetAddress() {
        when(eventRepository.save(validEvent)).thenReturn(validEvent);
        when(viacepClient.getAdress(validEvent.getCep())).thenReturn(null);

        EventResponseDto createdEvent = eventService.createEvent(validEventRequestDto);

        assertThat(createdEvent).isNotNull();
        assertThat(createdEvent.logradouro()).isNull();
        assertThat(createdEvent.bairro()).isNull();
        assertThat(createdEvent.cidade()).isNull();
        assertThat(createdEvent.uf()).isNull();

        verify(viacepClient).getAdress(validEvent.getCep());
        verify(eventRepository).save(validEvent);
    }

    @Test
    public void updateEvent_WithValidData_ReturnsUpdatedEvent() {
        when(eventRepository.findById(validEvent.getId())).thenReturn(java.util.Optional.of(validEvent));
        when(eventRepository.save(validEvent)).thenReturn(validEvent);

        EventRequestDto updateRequest = new EventRequestDto("Updated Event", TEST_DATE_TIME, "01153-000");
        EventResponseDto updatedEvent = eventService.updateEvent(validEvent.getId(), updateRequest);

        assertThat(updatedEvent.eventName()).isEqualTo("Updated Event");
        assertThat(updatedEvent.dateTime()).isEqualTo(TEST_DATE_TIME);

        verify(eventRepository).findById(validEvent.getId());
        verify(eventRepository).save(validEvent);
    }

    @Test
    public void updateEvent_WhenEventNotFound_ThrowsEntityNotFoundException() {
        when(eventRepository.findById(validEvent.getId())).thenReturn(java.util.Optional.empty());

        EventRequestDto updateRequest = new EventRequestDto("Updated Event", TEST_DATE_TIME, "01153-000");

        assertThatThrownBy(() -> eventService.updateEvent(validEvent.getId(), updateRequest))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessage("Event not found with ID: " + validEvent.getId());

        verify(eventRepository).findById(validEvent.getId());
        verify(eventRepository, never()).save(validEvent);
    }

    @Test
    public void deleteEvent_WhenEventExists_DeletesEvent() {
        when(eventRepository.findById(validEvent.getId())).thenReturn(java.util.Optional.of(validEvent));

        eventService.deleteEvent(validEvent.getId());

        verify(eventRepository).deleteById(validEvent.getId());
    }

    @Test
    public void deleteEvent_WhenEventNotFound_ThrowsEntityNotFoundException() {
        when(eventRepository.findById(validEvent.getId())).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> eventService.deleteEvent(validEvent.getId()))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessage("Event not found with ID: " + validEvent.getId());

        verify(eventRepository).findById(validEvent.getId());
        verify(eventRepository, never()).deleteById(validEvent.getId());
    }

}
