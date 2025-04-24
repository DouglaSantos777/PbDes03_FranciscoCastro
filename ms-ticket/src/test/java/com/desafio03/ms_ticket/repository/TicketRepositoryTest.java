package com.desafio03.ms_ticket.repository;

import com.desafio03.ms_ticket.model.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.desafio03.ms_ticket.common.TicketConstants.VALID_TICKET;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    public void afterEach() {
        ticketRepository.deleteAll();
    }

    @Test
    public void getTicket_ByExistingEmail_ReturnsTicket() {
        Ticket ticket = ticketRepository.save(VALID_TICKET);

        Optional<Ticket> ticketOpt = ticketRepository.findByCustomerMail(ticket.getCustomerMail());

        assertThat(ticketOpt).isNotEmpty();
        assertThat(ticketOpt.get()).isEqualTo(ticket);
    }

    @Test
    public void getTicket_ByUnexistingEmail_ReturnsEmpty() {
        Optional<Ticket> ticketOpt = ticketRepository.findByCustomerMail("emailfake@example.com");

        assertThat(ticketOpt).isEmpty();
    }

    @Test
    public void getTickets_ByExistingCpf_ReturnsTickets() {
        Ticket ticket = ticketRepository.save(VALID_TICKET);

        List<Ticket> tickets = ticketRepository.findByCpf(ticket.getCpf());

        assertThat(tickets).isNotEmpty();
        assertThat(tickets).contains(ticket);
    }

    @Test
    public void getTickets_ByUnexistingCpf_ReturnsEmpty() {
        List<Ticket> tickets = ticketRepository.findByCpf("000.000.000-00");

        assertThat(tickets).isEmpty();
    }

    @Test
    public void existsByEventId_WithExistingEventId_ReturnsTrue() {
        ticketRepository.save(VALID_TICKET);

        boolean exists = ticketRepository.existsByEventId("abcdefghijk");

        assertThat(exists).isTrue();
    }

    @Test
    public void existsByEventId_WithUnexistingEventId_ReturnsFalse() {
        boolean exists = ticketRepository.existsByEventId("invalid id");

        assertThat(exists).isFalse();
    }

}