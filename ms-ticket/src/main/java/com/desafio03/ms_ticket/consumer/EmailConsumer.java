package com.desafio03.ms_ticket.consumer;

import com.desafio03.ms_ticket.dto.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailConsumer {

    @RabbitListener(queues = "${broker.queue.email.name}", ackMode = "MANUAL")
    public void listenEmail(@Payload EmailDto emailDto) {
        log.info("Mensagem recebida na fila: {}", emailDto);
        log.info("Enviando e-mail para: {}", emailDto.emailTo());

    }
}
