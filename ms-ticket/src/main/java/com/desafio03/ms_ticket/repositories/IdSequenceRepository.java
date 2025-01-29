package com.desafio03.ms_ticket.repositories;

import com.desafio03.ms_ticket.model.util.IdSequence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdSequenceRepository extends MongoRepository<IdSequence, String> {
}
