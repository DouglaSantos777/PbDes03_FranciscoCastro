package com.desafio03.ms_ticket.dto.mapper;

import com.desafio03.ms_ticket.dto.TicketRequestDto;
import com.desafio03.ms_ticket.dto.TicketResponseDto;
import com.desafio03.ms_ticket.feign.msevents.Event;
import com.desafio03.ms_ticket.feign.msevents.EventResponseDto;
import com.desafio03.ms_ticket.model.Ticket;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class TicketMapper {

    private static BigDecimal parseAmount(String amount) {
        String cleanedAmount = amount.replaceAll("[^\\d,]", "").replace(",", ".");
        return new BigDecimal(cleanedAmount);
    }

    public static Ticket toTicket(TicketRequestDto dto, Event event) {
        BigDecimal BRLtotalAmount = parseAmount(dto.BRLamount());
        BigDecimal USDtotalAmount = parseAmount(dto.USDamount());

        return Ticket.builder()
                .customerName(dto.customerName())
                .cpf(dto.cpf())
                .customerMail(dto.customerMail())
                .event(event)
                .BRLtotalAmount(BRLtotalAmount)
                .USDtotalAmount(USDtotalAmount)
                .status("concluído")
                .build();
    }

    private static String formatCurrency(BigDecimal amount, String currencySymbol) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String formatted = formatter.format(amount).replace("R$", currencySymbol).trim();
        return formatted.replace("\u00A0", " ");
    }

    public static TicketResponseDto toResponseDto(Ticket ticket) {
        String formattedBRL = formatCurrency(ticket.getBRLtotalAmount(), "R$");
        String formattedUSD = formatCurrency(ticket.getUSDtotalAmount(), "$");

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
                formattedBRL,
                formattedUSD,
                ticket.getStatus()
        );
    }
}
