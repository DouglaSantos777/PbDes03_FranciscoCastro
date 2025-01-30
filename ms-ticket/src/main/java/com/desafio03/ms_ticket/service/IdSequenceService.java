package com.desafio03.ms_ticket.service;

import com.desafio03.ms_ticket.model.IdSequence;
import com.desafio03.ms_ticket.repository.IdSequenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdSequenceService {

    private final IdSequenceRepository idSequenceRepository;

    @Transactional
    public Long getNextId() {
        IdSequence nextId = idSequenceRepository.findById("ticketId")
                .orElse(new IdSequence("ticketId", 0L));

        nextId.setNextId(nextId.getNextId() + 1);
        idSequenceRepository.save(nextId);

        return nextId.getNextId();
    }

}
