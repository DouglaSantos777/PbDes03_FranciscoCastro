package com.desafio03.ms_ticket.service;

import com.desafio03.ms_ticket.model.Email;
import com.desafio03.ms_ticket.repository.EmailRepository;
import com.desafio03.ms_ticket.enums.EmailStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final EmailRepository emailRepository;

    private final JavaMailSender javaMailSender;

    @Value(value = "${spring.mail.username}")
    private String emailSender;

    public Email sendEmail(Email email) {
        try {
            email.setSendDateEmail(LocalDateTime.now());
            email.setFromEmail(emailSender);
            email.setReplyTo(emailSender);
            email.setContentType("text/html");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.getEmailTo());
            message.setSubject(email.getSubject());
            message.setText(email.getText());
            javaMailSender.send(message);

            email.setStatus(EmailStatus.SENT);
        } catch (MailException e) {
            log.error("Erro ao enviar email: {}", e.getMessage(), e);
            email.setStatus(EmailStatus.FAILED);
        }

        return emailRepository.save(email);
    }
}