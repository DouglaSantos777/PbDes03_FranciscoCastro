package com.desafio03.ms_ticket.repository;

import com.desafio03.ms_ticket.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByCpf(String cpf);

    boolean existsByEventId(String eventId);

    Optional<Ticket> findByCustomerMail(String email);
}
