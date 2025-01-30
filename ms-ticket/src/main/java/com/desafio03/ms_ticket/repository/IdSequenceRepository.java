package com.desafio03.ms_ticket.repository;

import com.desafio03.ms_ticket.model.IdSequence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdSequenceRepository extends MongoRepository<IdSequence, String> {
}
