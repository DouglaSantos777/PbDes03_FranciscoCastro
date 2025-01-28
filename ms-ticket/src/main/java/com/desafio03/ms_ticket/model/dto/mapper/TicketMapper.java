package com.desafio03.ms_ticket.model.dto.mapper;

import com.desafio03.ms_ticket.clientevents.Event;
import com.desafio03.ms_ticket.clientevents.EventResponseDto;
import com.desafio03.ms_ticket.model.Ticket;
import com.desafio03.ms_ticket.model.dto.TicketRequestDto;
import com.desafio03.ms_ticket.model.dto.TicketResponseDto;

public class TicketMapper {

    public static Ticket toTicket(TicketRequestDto dto, Event event) {
        return Ticket.builder()
                .ticketId(dto.ticketId())
                .customerName(dto.customerName())
                .cpf(dto.cpf())
                .customerMail(dto.customerMail())
                .event(event)
                .BRLtotalAmount(dto.BRLamount())
                .USDtotalAmount(dto.USDamount())
                .status("concluído")
                .build();
    }

    public static TicketResponseDto toResponseDto(Ticket ticket) {
        String formattedBRL = ticket.getBRLtotalAmount().replaceAll("[^\\d,]", "").replace(",", ".");
        String formattedUSD = ticket.getUSDtotalAmount().replaceAll("[^\\d,]", "").replace(",", ".");

        double brlAmount = Double.parseDouble(formattedBRL);
        double usdAmount = Double.parseDouble(formattedUSD);

        String formattedBRLWithCurrency = String.format("R$ %.2f", brlAmount);
        String formattedUSDWithCurrency = String.format("$ %.2f", usdAmount);

        Event event = ticket.getEvent();

        EventResponseDto eventResponseDto = new EventResponseDto(
                event.id(),
                event.eventName(),
                event.dateTime(),
                event.logradouro(),
                event.bairro(),
                event.cidade(),
                event.uf()
        );

        return new TicketResponseDto(
                ticket.getTicketId(),
                ticket.getCpf(),
                ticket.getCustomerName(),
                ticket.getCustomerMail(),
                eventResponseDto,
                formattedBRLWithCurrency,
                formattedUSDWithCurrency,
                ticket.getStatus()
        );
    }
}
