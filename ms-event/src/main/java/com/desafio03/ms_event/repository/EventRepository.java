package com.desafio03.ms_event.repository;

import com.desafio03.ms_event.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {

}
