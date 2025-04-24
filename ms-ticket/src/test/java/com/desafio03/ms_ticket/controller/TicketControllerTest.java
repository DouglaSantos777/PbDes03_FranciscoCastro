package com.desafio03.ms_ticket.controller;

import com.desafio03.ms_ticket.common.TicketConstants;
import com.desafio03.ms_ticket.dto.TicketRequestDto;
import com.desafio03.ms_ticket.dto.TicketResponseDto;
import com.desafio03.ms_ticket.exception.TicketNotFoundException;
import com.desafio03.ms_ticket.feign.msevents.HasTicketResponseDto;
import com.desafio03.ms_ticket.model.Ticket;
import com.desafio03.ms_ticket.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    private Ticket validTicket;
    private TicketRequestDto validRequestDto;
    private TicketRequestDto invalidTicket;
    private TicketResponseDto validTicketResponse;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() {
        validTicket = TicketConstants.VALID_TICKET;
        validRequestDto = TicketConstants.VALID_TICKET_REQUEST_DTO;
        invalidTicket = TicketConstants.INVALID_TICKET_REQUEST_DTO;
        validTicketResponse = TicketConstants.VALID_TICKET_RESPONSE_DTO;
    }

    @Test
    public void createTicket_WithValidData_ReturnsCreatedTicketWithStatus201() throws Exception {
        when(ticketService.createTicket(any(TicketRequestDto.class))).thenReturn(validTicketResponse);

        mockMvc.perform(post("/api/v1/tickets/create-ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cpf").value(validTicketResponse.cpf()));

        verify(ticketService).createTicket(any(TicketRequestDto.class));
    }

    @Test
    public void createTicket_WithInvalidData_ReturnsStatus422() throws Exception {
        mockMvc.perform(post("/api/v1/tickets/create-ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTicket)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").exists());

        verify(ticketService, never()).createTicket(any(TicketRequestDto.class));
    }

    @Test
    public void getTicket_ByExistingId_ReturnsTicketWithStatus200() throws Exception {
        when(ticketService.getTicketById(anyString())).thenReturn(validTicketResponse);

        mockMvc.perform(get("/api/v1/tickets/get-ticket/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value(validTicketResponse.cpf()));

        verify(ticketService).getTicketById(anyString());
    }

    @Test
    public void getTicket_ByNonExistentId_ThrowsTicketNotFoundException() throws Exception {
        when(ticketService.getTicketById(anyString())).thenThrow(new TicketNotFoundException("Ticket not found"));

        mockMvc.perform(get("/api/v1/tickets/get-ticket/{id}", "invalidId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ticket not found"));

        verify(ticketService).getTicketById(anyString());
    }

    @Test
    public void getTicket_ByCpf_ReturnsListOfTicketsWithStatus200() throws Exception {
        when(ticketService.getTicketByCpf(anyString())).thenReturn(List.of(validTicketResponse));

        mockMvc.perform(get("/api/v1/tickets/get-ticket-by-cpf/{cpf}", "12345678900")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cpf").value(validTicketResponse.cpf()));

        verify(ticketService).getTicketByCpf(anyString());
    }

    @Test
    public void checkTicketsByEventId_WithValidEventId_ReturnsHasTicketResponseDto() throws Exception {
        String eventId = "abcdefghijk";
        HasTicketResponseDto responseDto = new HasTicketResponseDto(eventId,true);

        when(ticketService.checkTicketsByEventsId(eventId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/tickets/check-tickets-by-event/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasTickets").value(is(true)));

        verify(ticketService).checkTicketsByEventsId(eventId);
    }

    @Test
    public void updateTicket_WithValidData_ReturnsUpdatedTicketWithStatus200() throws Exception {
        when(ticketService.updateTicket(anyString(), any(TicketRequestDto.class))).thenReturn(validTicketResponse);

        mockMvc.perform(put("/api/v1/tickets/update-ticket/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value(validTicketResponse.cpf()));

        verify(ticketService).updateTicket(anyString(), any(TicketRequestDto.class));
    }

    @Test
    public void cancelTicket_ByExistingId_ReturnsCanceledTicketWithStatus200() throws Exception {
        when(ticketService.cancelTicketById(anyString())).thenReturn(validTicketResponse);

        mockMvc.perform(delete("/api/v1/tickets/cancel-ticket/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value(validTicketResponse.cpf()));

        verify(ticketService).cancelTicketById(anyString());
    }

    @Test
    public void cancelTicket_ByCpf_ReturnsListOfCanceledTicketsWithStatus200() throws Exception {
        when(ticketService.cancelTicketByCpf(anyString())).thenReturn(List.of(validTicketResponse));

        mockMvc.perform(delete("/api/v1/tickets/cancel-ticket-by-cpf/{cpf}", "12345678900")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cpf").value(validTicketResponse.cpf()));

        verify(ticketService).cancelTicketByCpf(anyString());
    }
}
