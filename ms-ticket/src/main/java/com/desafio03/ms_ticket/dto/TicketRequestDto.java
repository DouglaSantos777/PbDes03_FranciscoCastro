package com.desafio03.ms_ticket.dto;

import jakarta.validation.constraints.*;

public record TicketRequestDto(
        @NotBlank(message = "Customer Name cannot be blank. Please provide the customer name.")
        @Size(min = 5, max = 100, message = "Customer Name must be between 3 and 100 characters.")
        String customerName,

        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must be in the format XXX.XXX.XXX-XX")
        @NotBlank(message = "CPF cannot be blank. Please provide a valid CPF.")
        String cpf,

        @Email(message = "Invalid email format. Please provide a valid email address.")
        @NotBlank(message = "Email cannot be blank. Please provide the customer's email address.")
        String customerMail,

        @NotBlank(message = "Event ID cannot be empty. Please provide the event ID.")
        @Size(min = 5, max = 20, message = "Event ID must be between 5 and 20 characters.")
        String eventId,

        @NotBlank(message = "Event Name cannot be blank. Please provide the event name.")
        @Size(min = 3, max = 100, message = "Event Name must be between 3 and 100 characters.")
        String eventName,

        @Pattern(regexp = "R\\$\\s?\\d{1,10}(,\\d{1,2})?", message = "BRLamount must be a valid amount (e.g., R$ 60,60).")
        String BRLamount,

        @Pattern(regexp = "\\$\\s?\\d{1,10}(,\\d{1,2})?", message = "USDamount must be a valid amount (e.g., $ 10,10).")
        String USDamount
){
}

