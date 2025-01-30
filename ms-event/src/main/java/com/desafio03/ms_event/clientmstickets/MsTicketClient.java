package com.desafio03.ms_event.clientmstickets;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-ticket", url = "http://localhost:8081/api/v1/tickets")
public interface MsTicketClient {

    @GetMapping("check-tickets-by-event/{eventId}")
    HasTicketResponse checkTicketsByEvent(@PathVariable String eventId);
}
