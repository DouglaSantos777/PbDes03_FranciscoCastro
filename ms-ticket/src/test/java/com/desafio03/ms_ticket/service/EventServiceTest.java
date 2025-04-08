/*
package com.desafio03.ms_ticket.service;

import com.desafio03.ms_event.common.EventConstants;
import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.dto.EventResponseDto;
import com.desafio03.ms_event.exception.EventConflictException;
import com.desafio03.ms_event.exception.EventNotFoundException;
import com.desafio03.ms_event.exception.EventWithTicketsException;
import com.desafio03.ms_event.feign.msticket.HasTicketResponse;
import com.desafio03.ms_event.feign.msticket.MsTicketClient;
import com.desafio03.ms_event.feign.viacep.Address;
import com.desafio03.ms_event.feign.viacep.ViacepClient;
import com.desafio03.ms_event.model.Event;
import com.desafio03.ms_event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ViacepClient viacepClient;

    @Mock
    private MsTicketClient msTicketClient;

    private Event validEvent;

    private EventRequestDto validEventRequestDto;

    private Address mockAddress;

    private LocalDateTime testDateTime;

    @BeforeEach
    public void setUp() {
        validEvent = EventConstants.VALID_EVENT;
        validEventRequestDto = EventConstants.VALID_EVENT_REQUEST_DTO;
        mockAddress = EventConstants.VALID_ADDRESS;
        testDateTime = EventConstants.TEST_DATE_TIME;
    }

    @Test
    public void createEvent_WithValidData_ReturnsCreatedEvent() {
        when(eventRepository.save(validEvent)).thenReturn(validEvent);

        when(viacepClient.getAddress(validEvent.getCep())).thenReturn(mockAddress);

        EventResponseDto createdEvent = eventService.createEvent(validEventRequestDto);

        EventResponseDto createdEventResponse = EventConstants.VALID_EVENT_RESPONSE_DTO;

        assertThat(createdEvent).isNotNull();
        assertThat(createdEvent).isEqualTo(createdEventResponse);

        verify(viacepClient).getAddress(validEvent.getCep());
        verify(eventRepository).save(validEvent);
    }

    @Test
    public void createEvent_WithCepNull_ReturnsCreatedEventWithAddressFieldsNull() {
        when(eventRepository.save(validEvent)).thenReturn(validEvent);
        when(viacepClient.getAddress(validEvent.getCep())).thenReturn(null);

        EventResponseDto createdEvent = eventService.createEvent(validEventRequestDto);

        assertThat(createdEvent).isNotNull();
        assertThat(createdEvent.logradouro()).isNull();
        assertThat(createdEvent.bairro()).isNull();
        assertThat(createdEvent.cidade()).isNull();
        assertThat(createdEvent.uf()).isNull();

        verify(viacepClient).getAddress(validEvent.getCep());
        verify(eventRepository).save(validEvent);
    }

    @Test
    public void createEvent_WithConflictInCepAndDate_ThrowsEventConflictException() {
        when(eventRepository.findAll()).thenReturn(List.of(validEvent));

        EventRequestDto conflictEvent = new EventRequestDto(
                "Event with same cep and date of validEvent",
                validEvent.getDateTime(),
                validEvent.getCep()
        );

        assertThatThrownBy(() -> eventService.createEvent(conflictEvent))
                .isInstanceOf(EventConflictException.class);

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    public void getEvent_ByExistingId_ReturnsEvent() {
        when(eventRepository.findById(validEvent.getId())).thenReturn(Optional.ofNullable(validEvent));

        EventResponseDto event = eventService.getEvent(validEvent.getId());

        assertThat(event).isNotNull();
        assertThat(event.eventName()).isEqualTo(validEvent.getEventName());

        verify(eventRepository).findById(validEvent.getId());
    }

    @Test
    public void getEvent_ByNonExistentId_ThrowsEventNotFoundException() {
        when(eventRepository.findById("NonExistentId")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getEvent("NonExistentId"))
                .isInstanceOf(EventNotFoundException.class);

        verify(eventRepository).findById("NonExistentId");
    }

    @Test
    public void getAllEvents_WithEvents_ReturnsAllEvents() {
        when(eventRepository.findAll()).thenReturn(List.of(validEvent));

        List<EventResponseDto> events = eventService.getAllEvents();

        assertThat(events).isNotNull();
        assertThat(events.size()).isEqualTo(1);
        assertThat(events.get(0).eventName()).isEqualTo(validEvent.getEventName());

        verify(eventRepository).findAll();
    }

    @Test
    public void getAllEvents_WithNoEvents_ReturnsNoEvents() {
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        List<EventResponseDto> events = eventService.getAllEvents();

        assertThat(events).isNotNull();
        assertThat(events).isEmpty();
        verify(eventRepository).findAll();
    }

    @Test
    public void getAllEventsSorted_WithNoEvents_ReturnsEmptyList() {
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        List<EventResponseDto> events = eventService.getAllEventsSorted();

        assertThat(events).isNotNull();
        assertThat(events).isEmpty();

        verify(eventRepository).findAll();
    }

    @Test
    public void getAllEventsSorted_WithSameNameEvents_ReturnsCorrectlyOrderedEvents() {
        Event eventWithSameName = Event.builder()
                .eventName("Valid Event")
                .dateTime(testDateTime.minusDays(2))
                .cep("01153-002")
                .build();

        when(eventRepository.findAll()).thenReturn(List.of(validEvent, eventWithSameName));

        List<EventResponseDto> events = eventService.getAllEventsSorted();

        assertThat(events).isNotNull();
        assertThat(events.size()).isEqualTo(2);
        assertThat(events.get(0).eventName()).isEqualTo("Valid Event");
        assertThat(events.get(1).eventName()).isEqualTo("Valid Event");

        verify(eventRepository).findAll();
    }

    @Test
    public void updateEvent_WithValidData_ReturnsUpdatedEvent() {
        when(eventRepository.findById(validEvent.getId())).thenReturn(Optional.ofNullable(validEvent));
        when(eventRepository.save(validEvent)).thenReturn(validEvent);

        EventRequestDto updateRequest = new EventRequestDto("Updated Event", testDateTime, "01153-000");
        EventResponseDto updatedEvent = eventService.updateEvent(validEvent.getId(), updateRequest);

        assertThat(updatedEvent.eventName()).isEqualTo("Updated Event");
        assertThat(updatedEvent.dateTime()).isEqualTo(testDateTime);

        verify(eventRepository).findById(validEvent.getId());
        verify(eventRepository).save(validEvent);
    }

    @Test
    public void updateEvent_WithEventNotFound_ThrowsEventNotFoundException() {
        when(eventRepository.findById(validEvent.getId())).thenReturn(Optional.empty());

        EventRequestDto updateRequest = new EventRequestDto("Updated Event", testDateTime, "01153-000");

        assertThatThrownBy(() -> eventService.updateEvent(validEvent.getId(), updateRequest))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessage("Event not found with ID: " + validEvent.getId());

        verify(eventRepository).findById(validEvent.getId());
        verify(eventRepository, never()).save(validEvent);
    }

    @Test
    public void deleteEvent_WithEventExistsAndHasNoTickets_doesNotThrowAnyException() {
        when(eventRepository.findById(validEvent.getId())).thenReturn(Optional.of(validEvent));
        when(msTicketClient.checkTicketsByEvent(validEvent.getId())).thenReturn(new HasTicketResponse(validEvent.getId(), false));

        eventService.deleteEvent(validEvent.getId());

        verify(eventRepository).deleteById(validEvent.getId());
        assertThatCode(() -> eventService.deleteEvent(validEvent.getId())).doesNotThrowAnyException();
    }

    @Test
    public void deleteEvent_WithEventNotFound_ThrowsEventNotFoundException() {
        when(eventRepository.findById(validEvent.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.deleteEvent(validEvent.getId()))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessage("Event not found with ID: " + validEvent.getId());

        verify(eventRepository).findById(validEvent.getId());
        verify(eventRepository, never()).deleteById(validEvent.getId());
    }

    @Test
    public void deleteEvent_WithEventHasTicket_ThrowsEventWithTicketsException() {
        when(eventRepository.findById(validEvent.getId())).thenReturn(Optional.of(validEvent));
        when(msTicketClient.checkTicketsByEvent(validEvent.getId()))
                .thenReturn(new HasTicketResponse(validEvent.getId(), true));
                
                assertThatThrownBy(() -> eventService.deleteEvent(validEvent.getId()))
                .isInstanceOf(EventWithTicketsException.class)
                .hasMessage("The event with ID " + validEvent.getId() + " can't be deleted because it has tickets.");

        verify(eventRepository, never()).deleteById(validEvent.getId());
        verify(msTicketClient).checkTicketsByEvent(validEvent.getId());
    }
}
*/