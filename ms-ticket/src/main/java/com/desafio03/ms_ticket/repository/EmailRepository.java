package com.desafio03.ms_ticket.repository;

import com.desafio03.ms_ticket.model.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<Email, String>  {
}
