package com.desafio03.ms_ticket.feign.msevents;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-event", url = "http://localhost:8080/api/v1/events")
public interface EventClient {

    @GetMapping("/get-event/{id}")
    Event getEventById(@PathVariable String id);

    @GetMapping("/get-all-events")
    List<Event> getAllEvents();
}
