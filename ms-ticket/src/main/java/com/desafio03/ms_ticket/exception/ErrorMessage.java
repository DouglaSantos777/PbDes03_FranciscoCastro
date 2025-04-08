package com.desafio03.ms_ticket.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
public class ErrorMessage {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public ErrorMessage(HttpStatus status, HttpServletRequest request, String error) {
        this.timestamp = Instant.now();
        this.status = status.value();
        this.error =  status.getReasonPhrase();
        this.path = request.getRequestURI();
        this.message = error;
    }

    public ErrorMessage(HttpStatus status, HttpServletRequest request, String error, BindingResult result) {
        this.timestamp = Instant.now();
        this.status = status.value();
        this.error =  status.getReasonPhrase();
        this.path = request.getRequestURI();
        this.message = error;
        extractErrors(result);
    }

    private void extractErrors(BindingResult bindingResult) {
        this.errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            this.errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
