package com.desafio03.ms_ticket.repository;

import com.desafio03.ms_ticket.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    Optional<Ticket> findByCpf(String cpf);

    boolean existsByEventId(String eventId);
}
