/*
package com.desafio03.ms_ticket.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.template.default-receive-queue}")
    private String queue;

    @Bean
    public Queue queue() {
        return new Queue(queue, true);
    }
}
*/