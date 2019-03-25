package com.crud.tasks.mapper;

import com.crud.tasks.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TrelloMapperTest {
    @InjectMocks
    private TrelloMapper trelloMapper;

    @Test
    public void mapToBoardsTest() {
        // Given
        List<TrelloListDto> trelloListDtos = new ArrayList<>();
        trelloListDtos.add(new TrelloListDto("id", "name", true));
        List<TrelloBoardDto> trelloBoardDtos = new ArrayList<>();
        trelloBoardDtos.add(new TrelloBoardDto("id", "name", trelloListDtos));

        // When
        List<TrelloBoard> trelloBoards = trelloMapper.mapToBoards(trelloBoardDtos);

        // Then
        assertEquals(1, trelloBoards.size());
    }

    @Test
    public void mapToBoardsDtoTest() {
        // Given
        List<TrelloList> trelloLists = new ArrayList<>();
        trelloLists.add(new TrelloList("id", "name", true));
        List<TrelloBoard> trelloBoards = new ArrayList<>();
        trelloBoards.add(new TrelloBoard("id", "name", trelloLists));

        // When
        List<TrelloBoardDto> trelloBoardDtos = trelloMapper.mapToBoardsDto(trelloBoards);

        // Then
        assertEquals(1, trelloBoardDtos.size());
    }

    @Test
    public void mapToListTest() {
        // Given
        List<TrelloListDto> trelloListDtos = new ArrayList<>();
        trelloListDtos.add(new TrelloListDto("id", "name", true));

        // When
        List<TrelloList> trelloLists = trelloMapper.mapToList(trelloListDtos);

        // Then
        assertEquals(1, trelloLists.size());
    }

    @Test
    public void mapToListDtoTest() {
        // Given
        List<TrelloList> trelloLists = new ArrayList<>();
        trelloLists.add(new TrelloList("id", "name", true));

        // When
        List<TrelloListDto> trelloListDtos = trelloMapper.mapToListDto(trelloLists);

        // Then
        assertEquals(1, trelloListDtos.size());
    }

    @Test
    public void mapToCardDtoTest() {
        // Given
        TrelloCard trelloCard = new TrelloCard("name", "description", "pos", "listId");

        // When
        TrelloCardDto trelloCardDto = trelloMapper.mapToCardDto(trelloCard);

        // Then
        assertEquals("name", trelloCardDto.getName());
        assertEquals("description", trelloCardDto.getDescription());
        assertEquals("pos", trelloCardDto.getPos());
        assertEquals("listId", trelloCardDto.getListId());
    }

    @Test
    public void mapToCardTest() {
        // Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("name", "description", "pos", "listId");

        // When
        TrelloCard trelloCard = trelloMapper.mapToCard(trelloCardDto);

        // Then
        assertEquals("name", trelloCard.getName());
        assertEquals("description", trelloCard.getDescription());
        assertEquals("pos", trelloCard.getPos());
        assertEquals("listId", trelloCard.getListId());
    }
}
