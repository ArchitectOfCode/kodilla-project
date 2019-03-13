package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SimpleEmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMailMessage.class);

    @Autowired
    private JavaMailSender javaMailSender;

    // Sending simple e-mail for e-mail settings verification
    /*public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }*/

    public void send(final Mail mail) {
        LOGGER.info("Starting e-mail preparation...");
        try {
            javaMailSender.send(createMailMessage(mail));
            LOGGER.info("E-mail has been sent.");
        } catch (MailException me) {
            LOGGER.error("Failed to process e-mail sending: ", me.getMessage(), me);
        }
    }

    private SimpleMailMessage createMailMessage(final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        if(!mail.getCCTo().isEmpty() && mail.getCCTo().length() > 0) {
            mailMessage.setCc(mail.getCCTo());
        }
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getMessage());
        return mailMessage;
    }
}