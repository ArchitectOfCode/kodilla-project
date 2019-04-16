package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SimpleEmailServiceTest {
    @InjectMocks
    private SimpleEmailService simpleEmailService;

    @Mock
    private JavaMailSender javaMailSender;

    // To perform below old tests, set javaMailSender.send(createMailMessage(mail))
    // instead javaMailSender.send(createMimeMessage(mail)) in SimpleEmailService.java
    
    @Test
    public void shouldSendEmail() {
        // Given
        Mail mail = new Mail("onetimeuse@o2.pl", "",
                "Test e-mail has been sent sent by Spring application",
                "This is a test e-mail sent by application written in Java and Spring framework.");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        if(!mail.getCCTo().isEmpty() && mail.getCCTo().length() > 0) {
            mailMessage.setCc(mail.getCCTo());
        }
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getMessage());

        // When
        simpleEmailService.send(mail);

        // Then
        verify(javaMailSender, times(1)).send(mailMessage);
    }

    @Test
    public void shouldSendEmailWithCC() {
        // Given
        Mail mail = new Mail("onetimeuse@o2.pl", "onetimeuse@prokonto.pl",
                "Test e-mail has been sent sent by Spring application",
                "This is a test e-mail sent by application written in Java and Spring framework.");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        if(!mail.getCCTo().isEmpty() && mail.getCCTo().length() > 0) {
            mailMessage.setCc(mail.getCCTo());
        }
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getMessage());

        // When
        simpleEmailService.send(mail);

        // Then
        verify(javaMailSender, times(1)).send(mailMessage);
    }
}