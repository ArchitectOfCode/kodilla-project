package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TrelloMapper;
import com.crud.tasks.trello.client.TrelloClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrelloServiceTest {
    @InjectMocks
    private TrelloService trelloService;

    @Mock
    private TrelloClient trelloClient;

    @Mock
    private SimpleEmailService emailService;

    @Mock
    private AdminConfig adminConfig;


    @Test
    public void createTrelloCardTest() {
        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("Card", "Trello Card", "1", "1");
        Trello trello = new Trello(1, 1);
        AttachmentsByType attachmentsByType = new AttachmentsByType(trello);
        TrelloBadgeDto trelloBadgeDto = new TrelloBadgeDto(3, attachmentsByType);
        CreatedTrelloCardDto createdTrelloCardDto =
                new CreatedTrelloCardDto("id", "name", "URL", trelloBadgeDto);

        when(trelloService.createTrelloCard(trelloCardDto)).thenReturn(createdTrelloCardDto);

        //When
        CreatedTrelloCardDto fetchedTrelloCardDto = trelloService.createTrelloCard(trelloCardDto);

        //Then
        assertEquals("id", fetchedTrelloCardDto.getId());
        assertEquals("name", fetchedTrelloCardDto.getName());
        assertEquals("URL", fetchedTrelloCardDto.getShortUrl());
        assertEquals(trelloBadgeDto, fetchedTrelloCardDto.getBadges());
    }
}
