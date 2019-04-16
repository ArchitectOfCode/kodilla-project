package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.Mail;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import com.crud.tasks.domain.CreatedTrelloCardDto;
import com.crud.tasks.trello.client.TrelloClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
public class TrelloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMailMessage.class);

    @Autowired
    private TrelloClient trelloClient;

    @Autowired
    private SimpleEmailService emailService;

    @Autowired
    private AdminConfig adminConfig;

    private static final String SUBJECT = "Tasks: New Trello Card";

    public List<TrelloBoardDto> fetchTrelloBoards() {
        return trelloClient.getTrelloBoards();
    }

    // Sending simple e-mail for e-mail settings verification
    /*public CreatedTrelloCardDto createTrelloCard(final TrelloCardDto trelloCardDto) {
        CreatedTrelloCardDto newCard = trelloClient.createNewCard(trelloCardDto);
        LOGGER.info("Sending test e-mail...");
        try {
            emailService.sendSimpleMessage(adminConfig.getAdminMail(), "test", "Wednesday test");
        } catch (Exception e) {
            LOGGER.error("Exception occur: " + e);
        }
        LOGGER.info("Test e-mail should be sent.");
    }*/

    public CreatedTrelloCardDto createTrelloCard(final TrelloCardDto trelloCardDto) {
        CreatedTrelloCardDto newCard = trelloClient.createNewCard(trelloCardDto);
        ofNullable(newCard).ifPresent(card -> emailService.send(new Mail(adminConfig.getAdminMail(),
                adminConfig.getAdminDeputyMail(),
                SUBJECT,
                "New card: " + trelloCardDto.getName() + " has been created on your Trello account"),
                newCard.getShortUrl()));
        return newCard;
    }
}
