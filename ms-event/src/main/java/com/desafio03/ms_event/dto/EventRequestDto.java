package com.desafio03.ms_event.dto;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record EventRequestDto(
        @NotNull(message = "Event Name is required")
        String eventName,
        @NotNull(message = "Date Time is required")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dateTime,
        @NotNull(message = "cep is required")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Invalid CEP format")
        String cep
) {
}
