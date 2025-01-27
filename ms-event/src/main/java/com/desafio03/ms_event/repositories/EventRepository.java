package com.desafio03.ms_event.repositories;

import com.desafio03.ms_event.dto.EventRequestDto;
import com.desafio03.ms_event.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
    boolean existsByEventName(String eventName);
}
