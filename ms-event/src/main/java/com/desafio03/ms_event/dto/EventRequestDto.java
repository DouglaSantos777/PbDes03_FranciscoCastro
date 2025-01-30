package com.desafio03.ms_event.dto;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record EventRequestDto(
        @NotBlank(message = "Event Name cannot be blank. Please provide the event name.")
        @Size(min = 3, max = 100, message = "Event Name must be between 3 and 100 characters.")
        String eventName,

        @NotNull(message = "Date Time cannot be null.")
        @Future(message = "Event DateTime must be in the future.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dateTime,

        @NotBlank(message = "CEP cannot be blank. Please provide a valid CEP.")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Invalid CEP format")
        String cep
) {
}
