package com.desafio03.ms_event.exception;

public class EventConflictException extends RuntimeException {
    public EventConflictException(String msg) {
        super(msg);
    }
}