package com.desafio03.ms_ticket.exception;

public class CpfMismatchException extends RuntimeException {
    public CpfMismatchException(String msg) {
        super(msg);
    }
}