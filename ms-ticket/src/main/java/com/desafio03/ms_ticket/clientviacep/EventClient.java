package com.desafio03.ms_ticket.clientviacep;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-event", url = "http://localhost:8080/api/v1/events")
public interface EventClient {

    @GetMapping("/get-event/{id}")
    EventResponseDto getEventById(@PathVariable String id);
}
