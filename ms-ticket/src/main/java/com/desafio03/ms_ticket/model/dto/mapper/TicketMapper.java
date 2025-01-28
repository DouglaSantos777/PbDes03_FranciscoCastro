package com.desafio03.ms_ticket.model.dto.mapper;

import com.desafio03.ms_ticket.clientevents.EventResponseDto;
import com.desafio03.ms_ticket.model.Ticket;
import com.desafio03.ms_ticket.model.dto.TicketRequestDto;
import com.desafio03.ms_ticket.model.dto.TicketResponseDto;

public class TicketMapper {

    public static Ticket toTicket(TicketRequestDto dto, EventResponseDto eventResponseDto) {
        return Ticket.builder()
                .ticketId(dto.ticketId())
                .customerName(dto.customerName())
                .cpf(dto.cpf())
                .customerMail(dto.customerMail())
                .event(eventResponseDto)
                .BRLtotalAmount(dto.BRLtotalAmount())
                .USDtotalAmount(dto.USDtotalAmount())
                .status("concluído")
                .build();
    }

    public static TicketResponseDto toResponseDto(Ticket ticket) {
        return new TicketResponseDto(
                ticket.getTicketId(),
                ticket.getCpf(),
                ticket.getCustomerName(),
                ticket.getCustomerMail(),
                ticket.getEvent(),
                ticket.getBRLtotalAmount(),
                ticket.getUSDtotalAmount(),
                ticket.getStatus()
        );
    }
}
