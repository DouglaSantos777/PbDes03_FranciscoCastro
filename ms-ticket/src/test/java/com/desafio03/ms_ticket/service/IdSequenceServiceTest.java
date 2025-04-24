package com.desafio03.ms_ticket.service;

import com.desafio03.ms_ticket.model.IdSequence;
import com.desafio03.ms_ticket.repository.IdSequenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdSequenceServiceTest {

    @Mock
    private IdSequenceRepository idSequenceRepository;

    @InjectMocks
    private IdSequenceService idSequenceService;

    @Test
    void shouldReturnNextIdWhenExistingIdSequence() {
        IdSequence existingSequence = new IdSequence("ticketId", 10L);
        when(idSequenceRepository.findById("ticketId")).thenReturn(Optional.of(existingSequence));

        Long nextId = idSequenceService.getNextId();

        assertEquals(11L, nextId);
        verify(idSequenceRepository).save(existingSequence);
    }

    @Test
    void shouldReturnOneWhenIdSequenceDoesNotExist() {
        when(idSequenceRepository.findById("ticketId")).thenReturn(Optional.empty());

        Long nextId = idSequenceService.getNextId();

        assertEquals(1L, nextId);
        verify(idSequenceRepository).save(any(IdSequence.class));
    }
}
