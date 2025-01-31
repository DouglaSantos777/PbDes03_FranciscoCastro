package com.desafio03.ms_ticket.consumer;

import com.desafio03.ms_ticket.dto.EmailDto;
import com.desafio03.ms_ticket.model.Email;
import com.desafio03.ms_ticket.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailConsumer {

    final EmailService emailService;

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmail(@Payload EmailDto emailDto) {
        log.info("Mensagem recebida na fila: {}", emailDto);

        var email = new Email();
        BeanUtils.copyProperties(emailDto, email);

        log.info("Chamando serviço de envio de e-mail para: {}", email.getEmailTo());
        emailService.sendEmail(email);
        log.info("E-mail processado com sucesso.");
    }
}
