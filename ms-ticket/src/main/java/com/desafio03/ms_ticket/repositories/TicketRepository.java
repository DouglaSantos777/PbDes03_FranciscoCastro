package com.desafio03.ms_ticket.repositories;

import com.desafio03.ms_ticket.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String> {
}
