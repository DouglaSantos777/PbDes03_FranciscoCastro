package com.desafio03.ms_ticket.producer;

import com.desafio03.ms_ticket.dto.EmailDto;
import com.desafio03.ms_ticket.model.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TicketProducer {

    final RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public void publishEmail(Ticket ticket) {
        EmailDto emailDto = new EmailDto(
                ticket.getTicketId(),
                ticket.getCustomerMail(),
                "Confirmação de Compra",
                "Sua compra do ingress para o evento " + ticket.getEvent().eventName()
                        + " foi realizada com sucesso! Te espero lá! \nInformações do ingresso: "
                        + ticket.toString()
        );

        log.info("Enviando mensagem para a fila {}: {}", routingKey, emailDto);
        rabbitTemplate.convertAndSend(routingKey, emailDto);
        log.info("Mensagem enviada com sucesso.");

    }
}
