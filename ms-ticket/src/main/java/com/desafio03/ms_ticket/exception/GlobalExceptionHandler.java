package com.desafio03.ms_ticket.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEventNotFoundException(EventNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorMessage response = new ErrorMessage(status, request, e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleTicketNotFoundException(TicketNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorMessage response = new ErrorMessage(status, request, e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request,
                                                                            BindingResult result) {

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorMessage response = new ErrorMessage(status, request, "invalid(s) field(s)", result);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(EmailMismatchException.class)
    public ResponseEntity<ErrorMessage> handleEmailMismatchException(EmailMismatchException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorMessage response = new ErrorMessage(status, request, e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(CpfMismatchException.class)
    public ResponseEntity<ErrorMessage> handleCpfMismatchException(CpfMismatchException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorMessage response = new ErrorMessage(status, request, e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleUnexpectedException(RuntimeException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        ErrorMessage response = new ErrorMessage(status, request, "An internal error occurred. Please try again later.");
        return ResponseEntity.status(status).body(response);
    }

}
