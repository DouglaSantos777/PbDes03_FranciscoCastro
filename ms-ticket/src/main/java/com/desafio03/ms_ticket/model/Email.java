package com.desafio03.ms_ticket.model;

import com.desafio03.ms_ticket.status.EmailStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "emails")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "emailId")
public class Email {

    @Id
    private String emailId;
    private String fromEmail;
    private String emailTo;
    private String replyTo;
    private String subject;
    private String text;
    private String contentType;
    private LocalDateTime sendDateEmail;
    private EmailStatus status;

}
