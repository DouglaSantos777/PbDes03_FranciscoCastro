package com.desafio03.ms_ticket.service;

import com.desafio03.ms_ticket.enums.EmailStatus;
import com.desafio03.ms_ticket.model.Email;
import com.desafio03.ms_ticket.repository.EmailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void shouldSendEmailSuccessfully() {
        Email email = new Email();
        email.setEmailTo("test@email.com");
        email.setSubject("Test Subject");
        email.setText("Test Body");

        when(emailRepository.save(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Email sentEmail = emailService.sendEmail(email);

        assertEquals(EmailStatus.SENT, sentEmail.getStatus());
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(email);
    }

    @Test
    void shouldMarkEmailAsFailedWhenExceptionOccurs() {
        Email email = new Email();
        email.setEmailTo("test@email.com");
        email.setSubject("Test Subject");
        email.setText("Test Body");

        doThrow(new MailException("Error sending email") {}).when(javaMailSender).send(any(SimpleMailMessage.class));
        when(emailRepository.save(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Email failedEmail = emailService.sendEmail(email);

        assertEquals(EmailStatus.FAILED, failedEmail.getStatus());
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(email);
    }
}
