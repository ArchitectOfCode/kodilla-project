package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SimpleEmailServiceTest {
    private static final String MAIL_TO = "onetimeuse@o2.pl";
    private static final String MAIL_CC = "onetimeuse@prokonto.pl";
    private static final String MAIL_SUBJECT = "Test e-mail has been sent sent by Spring application";
    private static final String MAIL_MSG = "This is a test e-mail sent by application written in Java and Spring framework.";

    @InjectMocks
    private SimpleEmailService simpleEmailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.ALL);

    // To perform below old tests, set javaMailSender.send(createMailMessage(mail))
    // instead of javaMailSender.send(createMimeMessage(mail)) in SimpleEmailService.java

    @Test
    public void shouldSendGreenMail() throws MessagingException {
        // Given
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(MAIL_TO);
        if(!MAIL_CC.isEmpty() && MAIL_CC.length() > 0) {
            mailMessage.setCc(MAIL_CC);
        }
        mailMessage.setSubject(MAIL_SUBJECT);
        mailMessage.setText(MAIL_MSG);

        // When
        GreenMailUtil.sendTextEmailTest(MAIL_TO, "", MAIL_SUBJECT, MAIL_MSG);
        //simpleEmailService.send(mail, "https://www.duckduckgo.com");

        // Then
        assertTrue(greenMail.waitForIncomingEmail(5000, 1));

        Message[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals(MAIL_SUBJECT, messages[0].getSubject());
        assertEquals(MAIL_MSG, GreenMailUtil.getBody(messages[0]));
    }

    @Test
    public void shouldSendEmail() {
        // Given
        Mail mail = new Mail(MAIL_TO, "", MAIL_SUBJECT, MAIL_MSG);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(MAIL_TO);
        mailMessage.setSubject(MAIL_SUBJECT);
        mailMessage.setText(MAIL_MSG);

        // When
        simpleEmailService.sendCurrentlyInDatabase(mail);
        //simpleEmailService.send(mail, "https://www.duckduckgo.com");

        // Then
        verify(javaMailSender, times(1)).send(mailMessage);
    }

    @Test
    public void shouldSendEmailWithCC() {
        // Given
        Mail mail = new Mail(MAIL_TO, MAIL_CC, MAIL_SUBJECT, MAIL_MSG);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(MAIL_TO);
        if(!MAIL_CC.isEmpty() && MAIL_CC.length() > 0) {
            mailMessage.setCc(MAIL_CC);
        }
        mailMessage.setSubject(MAIL_SUBJECT);
        mailMessage.setText(MAIL_MSG);

        // When
        //simpleEmailService.send(mail);
        simpleEmailService.sendCurrentlyInDatabase(mail);

        // Then
        verify(javaMailSender, times(1)).send(mailMessage);
    }
}