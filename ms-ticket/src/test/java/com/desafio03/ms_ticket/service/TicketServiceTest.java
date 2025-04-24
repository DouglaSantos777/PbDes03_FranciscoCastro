package com.desafio03.ms_ticket.service;

import com.desafio03.ms_ticket.common.TicketConstants;
import com.desafio03.ms_ticket.dto.TicketRequestDto;
import com.desafio03.ms_ticket.dto.TicketResponseDto;
import com.desafio03.ms_ticket.exception.CpfMismatchException;
import com.desafio03.ms_ticket.exception.EmailMismatchException;
import com.desafio03.ms_ticket.exception.EventNotFoundException;
import com.desafio03.ms_ticket.exception.TicketNotFoundException;
import com.desafio03.ms_ticket.feign.msevents.EventClient;
import com.desafio03.ms_ticket.feign.msevents.HasTicketResponseDto;
import com.desafio03.ms_ticket.model.Ticket;
import com.desafio03.ms_ticket.producer.TicketProducer;
import com.desafio03.ms_ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventClient eventClient;

    @Mock
    private IdSequenceService idSequenceService;

    @Mock
    private TicketProducer ticketProducer;

    private Ticket validTicket;
    private TicketRequestDto validRequestDto;

    private String expectedMessage;

    @BeforeEach
    public void setUp() {
        validTicket = TicketConstants.VALID_TICKET;
        validTicket.setStatus("concluído");
        validRequestDto = TicketConstants.VALID_TICKET_REQUEST_DTO;
        expectedMessage = "Id do ingresso: " + validTicket.getTicketId() + "\n" +
                "Cliente: " + validTicket.getCustomerName() + "\n" +
                "CPF: " + validTicket.getCpf() + "\n" +
                "Email do Cliente: " + validTicket.getCustomerMail() + "\n" +
                "Evento: " + validTicket.getEvent().eventName() + "\n" +
                "Data: " + validTicket.getEvent().dateTime().toLocalDate() + "\n" +
                "Hora: " + validTicket.getEvent().dateTime().toLocalTime() + "\n" +
                "Local: " + validTicket.getEvent().logradouro() + ", " +
                validTicket.getEvent().bairro() + ", " +
                validTicket.getEvent().cidade() + ", " +
                validTicket.getEvent().uf() + ", " +
                validTicket.getEvent().cep() + "\n" +
                "Valor Total (BRL): " + validTicket.getBRLtotalAmount() + "\n" +
                "Valor Total (USD): " + validTicket.getUSDtotalAmount() + "\n" +
                "Status: " + validTicket.getStatus();
    }

    @Test
    public void createTicket_WithValidData_ReturnsCreatedTicket() {
        when(eventClient.getEventById(validRequestDto.eventId())).thenReturn(TicketConstants.VALID_EVENT);
        when(ticketRepository.findByCpf(validRequestDto.cpf())).thenReturn(List.of());
        when(ticketRepository.findByCustomerMail(validRequestDto.customerMail())).thenReturn(Optional.empty());
        when(idSequenceService.getNextId()).thenReturn(1L);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(validTicket);

        TicketResponseDto createdTicket = ticketService.createTicket(validRequestDto);

        assertThat(createdTicket).isEqualTo(TicketConstants.VALID_TICKET_RESPONSE_DTO);

        verify(eventClient).getEventById(validTicket.getEvent().id());
        verify(ticketRepository).save(any(Ticket.class));
        verify(ticketRepository).save(validTicket);
        verify(ticketProducer).publishEmail(any(Ticket.class));

        assertThat(validTicket.toString()).isEqualTo(expectedMessage);
    }

    @Test
    public void createTicket_WithNonExistentEvent_ThrowsEventNotFoundException() {
        when(eventClient.getEventById(validRequestDto.eventId())).thenReturn(null);
        when(eventClient.getAllEvents()).thenReturn(List.of());

        assertThatThrownBy(() -> ticketService.createTicket(validRequestDto))
                .isInstanceOf(EventNotFoundException.class);

        verify(eventClient).getEventById(validRequestDto.eventId());
        verify(eventClient).getAllEvents();
    }

    @Test
    public void getNextId_ReturnsSequentialId() {
        when(idSequenceService.getNextId()).thenReturn(1L, 2L, 3L);

        long firstId = idSequenceService.getNextId();
        long secondId = idSequenceService.getNextId();
        long thirdId = idSequenceService.getNextId();

        assertThat(firstId).isEqualTo(1L);
        assertThat(secondId).isEqualTo(2L);
        assertThat(thirdId).isEqualTo(3L);

        verify(idSequenceService, times(3)).getNextId();
    }

    @Test
    public void getNextId_WithMultipleCalls_ReturnsUniqueIds() {
        when(idSequenceService.getNextId()).thenReturn(10L).thenReturn(20L).thenReturn(30L);

        long id1 = idSequenceService.getNextId();
        long id2 = idSequenceService.getNextId();
        long id3 = idSequenceService.getNextId();

        assertThat(id1).isEqualTo(10L);
        assertThat(id2).isEqualTo(20L);
        assertThat(id3).isEqualTo(30L);

        verify(idSequenceService, times(3)).getNextId();
    }

    @Test
    public void createTicket_WithCpfMismatch_ThrowsCpfMismatchException() {
        when(eventClient.getEventById(validRequestDto.eventId())).thenReturn(TicketConstants.VALID_EVENT);
        when(ticketRepository.findByCpf(validRequestDto.cpf())).thenReturn(List.of(
                Ticket.builder().customerName("Other Name").build()
        ));

        assertThatThrownBy(() -> ticketService.createTicket(validRequestDto))
                .isInstanceOf(CpfMismatchException.class);
    }

    @Test
    public void createTicket_WithEmailMismatch_ThrowsEmailMismatchException() {
        when(eventClient.getEventById(validRequestDto.eventId())).thenReturn(TicketConstants.VALID_EVENT);
        when(ticketRepository.findByCpf(validRequestDto.cpf())).thenReturn(List.of());
        when(ticketRepository.findByCustomerMail(validRequestDto.customerMail()))
                .thenReturn(Optional.of(Ticket.builder().cpf("000.000.000-00").build()));

        assertThatThrownBy(() -> ticketService.createTicket(validRequestDto))
                .isInstanceOf(EmailMismatchException.class);
    }

    @Test
    public void getTicketById_WithExistingId_ReturnsTicket() {
        when(ticketRepository.findById(validTicket.getTicketId())).thenReturn(Optional.of(validTicket));

        TicketResponseDto ticket = ticketService.getTicketById(validTicket.getTicketId());

        assertThat(ticket).isEqualTo(TicketConstants.VALID_TICKET_RESPONSE_DTO);

        verify(ticketRepository).findById(validTicket.getTicketId());
    }

    @Test
    public void getTicketById_WithNonExistentId_ThrowsTicketNotFoundException() {
        when(ticketRepository.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById("99"))
                .isInstanceOf(TicketNotFoundException.class);

        verify(ticketRepository).findById("99");
    }

    @Test
    public void updateTicket_WithValidData_UpdatesAndReturnsTicket() {
        when(ticketRepository.findById(validTicket.getTicketId())).thenReturn(Optional.of(validTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(validTicket);

        TicketRequestDto updateDto = new TicketRequestDto(
                validTicket.getCustomerName(),
                validTicket.getCpf(),
                "newemail@email.com",
                validTicket.getTicketId(),
                validTicket.getEvent().eventName(),
                "R$ 150,50",
                "$ 30,10"
        );

        TicketResponseDto updatedTicket = ticketService.updateTicket(validTicket.getTicketId(), updateDto);

        assertThat(updatedTicket.customerName()).isEqualTo("Valid User");
        assertThat(updatedTicket.customerMail()).isEqualTo("newemail@email.com");

        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    public void cancelTicketById_WithExistingTicket_SetsStatusToCanceled() {
        when(ticketRepository.findById(validTicket.getTicketId())).thenReturn(Optional.of(validTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(validTicket);

        TicketResponseDto canceledTicket = ticketService.cancelTicketById(validTicket.getTicketId());

        assertThat(canceledTicket.status()).isEqualTo("cancelado");

        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    public void cancelTicketById_WithNonExistentTicket_ThrowsTicketNotFoundException() {
        when(ticketRepository.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.cancelTicketById("99"))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    public void checkTicketsByEventsId_WithExistingTickets_ReturnsTrue() {
        when(ticketRepository.existsByEventId("eventbala")).thenReturn(true);

        HasTicketResponseDto response = ticketService.checkTicketsByEventsId("eventbala");

        assertThat(response.eventId()).isEqualTo("eventbala");
        assertThat(response.hasTickets()).isTrue();
    }

    @Test
    public void checkTicketsByEventsId_WithNoTickets_ReturnsFalse() {
        when(ticketRepository.existsByEventId("eventbala")).thenReturn(false);

        HasTicketResponseDto response = ticketService.checkTicketsByEventsId("eventbala");

        assertThat(response.eventId()).isEqualTo("eventbala");
        assertThat(response.hasTickets()).isFalse();
    }

    /*
    @Test
    public void cancelTicketByCpf_WithExistingTickets_CancelsAllTickets() {
        List<Ticket> tickets = List.of(
                Ticket.builder().ticketId("1").cpf("123.456.789-00").status("ativo").build(),
                Ticket.builder().ticketId("2").cpf("123.456.789-00").status("ativo").build()
        );

        when(ticketRepository.findByCpf("123.456.789-00")).thenReturn(tickets);
        when(ticketRepository.saveAll(anyList())).thenReturn(tickets);

        List<TicketResponseDto> canceledTickets = ticketService.cancelTicketByCpf("123.456.789-00");

        assertThat(canceledTickets).hasSize(2);
        assertThat(canceledTickets.get(0).status()).isEqualTo("cancelado");
        assertThat(canceledTickets.get(1).status()).isEqualTo("cancelado");

        verify(ticketRepository).saveAll(anyList());
    }
*/

    @Test
    public void cancelTicketByCpf_WithNoExistingTickets_ThrowsTicketNotFoundException() {
        when(ticketRepository.findByCpf("000.000.000-00")).thenReturn(List.of());

        assertThatThrownBy(() -> ticketService.cancelTicketByCpf("000.000.000-00"))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessageContaining("Ticket not found with cpf");

        verify(ticketRepository).findByCpf("000.000.000-00");
    }


    @Test
    public void getTicketByCpf_WithExistingTickets_ReturnsListOfTickets() {
        when(ticketRepository.findByCpf(validTicket.getCpf())).thenReturn(List.of(validTicket));

        List<TicketResponseDto> tickets = ticketService.getTicketByCpf(validTicket.getCpf());

        assertThat(tickets).hasSize(1);
        assertThat(tickets.get(0)).isEqualTo(TicketConstants.VALID_TICKET_RESPONSE_DTO);

        verify(ticketRepository).findByCpf(validTicket.getCpf());
    }

    @Test
    public void getTicketByCpf_WithNoExistingTickets_ReturnsEmptyList() {
        when(ticketRepository.findByCpf("000.000.000-00")).thenReturn(List.of());

        List<TicketResponseDto> tickets = ticketService.getTicketByCpf("000.000.000-00");

        assertThat(tickets).isEmpty();
    }

}
