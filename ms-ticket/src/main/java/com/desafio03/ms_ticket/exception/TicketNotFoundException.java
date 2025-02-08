package com.desafio03.ms_ticket.exception;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String msg) {
        super(msg);
    }
}
