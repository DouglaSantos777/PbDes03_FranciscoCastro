package com.desafio03.ms_ticket.controller;

import com.desafio03.ms_ticket.dto.TicketRequestDto;
import com.desafio03.ms_ticket.dto.TicketResponseDto;
import com.desafio03.ms_ticket.exception.ErrorMessage;
import com.desafio03.ms_ticket.feign.msevents.HasTicketResponseDto;
import com.desafio03.ms_ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ticket Manager", description = "APIs for managing tickets")
@RestController
@RequestMapping("api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Buy a new Ticket",
            description = "This endpoint allows to buy/create a new ticket.",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Ticket successfully created/purchased",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "409",
                            description = "A ticket with the provided CPF is already registered under another customer",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422",
                            description = "Invalid data provided",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("create-ticket")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponseDto createTicket(@Valid @RequestBody TicketRequestDto dto) {
        return ticketService.createTicket(dto);
    }

    @Operation(summary = "Get ticket by ID",
            description = "This endpoint returns an ticket by its unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Ticket successfully found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Ticket not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("get-ticket/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponseDto getTicketById(@PathVariable String id) {
        return ticketService.getTicketById(id);
    }

    @Operation(summary = "Get tickets by CPF",
            description = "This endpoint returns all tickets associated with a provided CPF.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Tickets successfully found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "No tickets found for the provided CPF",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("get-ticket-by-cpf/{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketResponseDto> getTicketByCpf(@PathVariable String cpf) {
        return ticketService.getTicketByCpf(cpf);
    }

    @Operation(summary = "Check if tickets by an event",
            description = "This endpoint checks tickets for a specific event by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Tickets availability checked successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HasTicketResponseDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Event not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("check-tickets-by-event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public HasTicketResponseDto checkTicketsByEventId(@PathVariable String eventId) {
        return ticketService.checkTicketsByEventsId(eventId);
    }


    @Operation(summary = "Update a ticket",
            description = "This endpoint allows updating the details of an existing ticket.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Ticket successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Ticket not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422",
                            description = "Invalid data provided",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping("update-ticket/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponseDto updateTicket(@PathVariable String id, @Valid @RequestBody TicketRequestDto dto) {
        return ticketService.updateTicket(id, dto);
    }

    @Operation(summary = "Cancel a ticket by ID",
            description = "This endpoint allows canceling a ticket by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Ticket successfully canceled",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Ticket not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @DeleteMapping("cancel-ticket/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponseDto cancelTicketById(@PathVariable String id) {
        return ticketService.cancelTicketById(id);
    }

    @Operation(summary = "Cancel tickets by CPF",
            description = "This endpoint allows canceling all tickets associated with a given CPF.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Tickets successfully canceled",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "404",
                            description = "No tickets found for the provided CPF",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @DeleteMapping("cancel-ticket-by-cpf/{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketResponseDto> cancelTicketByCpf(@PathVariable String cpf) {
        return ticketService.cancelTicketByCpf(cpf);
    }
}
