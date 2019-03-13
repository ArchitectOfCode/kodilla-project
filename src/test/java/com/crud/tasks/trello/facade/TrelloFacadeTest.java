package com.crud.tasks.trello.facade;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TrelloMapper;
import com.crud.tasks.service.TrelloService;
import com.crud.tasks.trello.validator.TrelloValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrelloFacadeTest {
    @InjectMocks
    private TrelloFacade trelloFacade;

    @Mock
    private TrelloService trelloService;

    @Mock
    private TrelloCard trelloCard;

    @Mock
    private TrelloList trelloList;

    @Mock
    private TrelloBoard trelloBoard;

    @Mock
    private TrelloMapper trelloMapper;

    @Mock
    private TrelloValidator trelloValidator;

    @Test
    public void mapToBoards() {
        //Given
        List<TrelloListDto> trelloListsDto = new ArrayList<>();
        TrelloListDto trelloListDto1 = new TrelloListDto("1", "Test List 1", false);
        TrelloListDto trelloListDto2 = new TrelloListDto("2", "Test List 2", false);
        trelloListsDto.add(trelloListDto1);
        trelloListsDto.add(trelloListDto2);

        List<TrelloBoardDto> trelloBoardsDto = new ArrayList<>();
        TrelloBoardDto telloBoardDto1 = new TrelloBoardDto("1", "Trello Board 1", trelloListsDto);
        TrelloBoardDto telloBoardDto2 = new TrelloBoardDto("2", "Trello Board 2", trelloListsDto);
        trelloBoardsDto.add(telloBoardDto1);
        trelloBoardsDto.add(telloBoardDto2);

        TrelloCardDto trelloCardDto = new TrelloCardDto("Card", "Trello Card", "1", "1");
        /*List<TrelloCardDto> trelloCardsDto = new ArrayList<>();
        trelloCardsDto.add(trelloCardDto);*/

        List<TrelloList> trelloLists = trelloMapper.mapToList(trelloListsDto);
        List<TrelloBoard> trelloBoards = trelloMapper.mapToBoards(trelloBoardsDto);
        TrelloCard trelloCard = trelloMapper.mapToCard(trelloCardDto);

        List<TrelloListDto> remappedTrelloListsDto = trelloMapper.mapToListDto(trelloLists);
        List<TrelloBoardDto> remappedTrelloBoardsDto = trelloMapper.mapToBoardsDto(trelloBoards);
        TrelloCardDto remappedTrelloCardDto = trelloMapper.mapToCardDto(trelloCard);

        /*List<TrelloList> trelloLists = new ArrayList<>();
        TrelloList trelloList1 = new TrelloList("1", )*/

        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoardsDto);
        when(trelloMapper.mapToBoards(trelloBoardsDto)).thenReturn(trelloBoards);
        when(trelloMapper.mapToBoardsDto(trelloBoards)).thenReturn(trelloBoardsDto);
        when(trelloMapper.mapToList(trelloListsDto)).thenReturn(trelloLists);
        when(trelloMapper.mapToListDto(trelloLists)).thenReturn(trelloListsDto);

        //When
        List<TrelloBoardDto> finalBoardList = trelloFacade.fetchTrelloBoards();

        //Then
        assertNotNull(trelloLists);
        assertNotNull(trelloListsDto);
        assertNotNull(trelloBoardsDto);
        assertNotNull(trelloBoards);

        System.out.println(trelloListsDto);
        System.out.println(trelloLists);

        assertEquals(trelloListsDto, remappedTrelloListsDto);
        assertEquals(trelloBoardsDto, remappedTrelloBoardsDto);
        assertEquals(trelloCardDto, remappedTrelloCardDto);


    }
}
