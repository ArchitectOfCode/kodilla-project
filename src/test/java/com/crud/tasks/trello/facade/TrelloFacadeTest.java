package com.crud.tasks.trello.facade;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TrelloMapper;
import com.crud.tasks.service.TrelloService;
import com.crud.tasks.trello.validator.TrelloValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private TrelloValidator trelloValidator;

    @Mock
    private TrelloMapper trelloMapper;

    @Test
    public void shouldFetchEmptyList() {
        //Given
        List<TrelloListDto> trelloListDtos = new ArrayList<>();
        trelloListDtos.add(new TrelloListDto("1", "Trello List 1", false));
        trelloListDtos.add(new TrelloListDto("2", "Trello List 2", false));

        List<TrelloBoardDto> trelloBoardDtos = new ArrayList<>();
        trelloBoardDtos.add(new TrelloBoardDto("1", "Trello Board 1", trelloListDtos));
        trelloBoardDtos.add(new TrelloBoardDto("2", "Trello Board 2", trelloListDtos));

        List<TrelloList> mappedTrelloLists = new ArrayList<>();
        mappedTrelloLists.add(new TrelloList("1", "Trello List 1", false));
        mappedTrelloLists.add(new TrelloList("2", "Trello List 2", false));

        List<TrelloBoard> mappedTrelloBoards = new ArrayList<>();
        mappedTrelloBoards.add(new TrelloBoard("1", "Trello Board 1", mappedTrelloLists));
        mappedTrelloBoards.add(new TrelloBoard("2", "Trello Board 2", mappedTrelloLists));

        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoardDtos);
        when(trelloMapper.mapToBoards(trelloBoardDtos)).thenReturn(mappedTrelloBoards);
        when(trelloMapper.mapToBoardsDto(anyList())).thenReturn(new ArrayList<>());
        when(trelloValidator.validateTrelloBoards(mappedTrelloBoards)).thenReturn(new ArrayList<>());

        //When
        List<TrelloBoardDto> fetchedTrelloBoardDtos = trelloFacade.fetchTrelloBoards();

        //Then
        assertNotNull(fetchedTrelloBoardDtos);
        assertEquals(0, fetchedTrelloBoardDtos.size());
    }

    @Test
    public void shouldFetchTrelloBoards() {
        //Given
        List<TrelloListDto> trelloListDtos = new ArrayList<>();
        trelloListDtos.add(new TrelloListDto("1", "Trello List 1", false));
        trelloListDtos.add(new TrelloListDto("2", "Trello List 2", false));

        List<TrelloBoardDto> trelloBoardDtos = new ArrayList<>();
        trelloBoardDtos.add(new TrelloBoardDto("1", "Trello Board 1", trelloListDtos));
        trelloBoardDtos.add(new TrelloBoardDto("2", "Trello Board 2", trelloListDtos));

        List<TrelloList> mappedTrelloLists = new ArrayList<>();
        mappedTrelloLists.add(new TrelloList("1", "Trello List 1", false));
        mappedTrelloLists.add(new TrelloList("2", "Trello List 2", false));

        List<TrelloBoard> mappedTrelloBoards = new ArrayList<>();
        mappedTrelloBoards.add(new TrelloBoard("1", "Trello Board 1", mappedTrelloLists));
        mappedTrelloBoards.add(new TrelloBoard("2", "Trello Board 2", mappedTrelloLists));

        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoardDtos);
        when(trelloMapper.mapToBoards(trelloBoardDtos)).thenReturn(mappedTrelloBoards);
        when(trelloMapper.mapToBoardsDto(anyList())).thenReturn(trelloBoardDtos);
        when(trelloValidator.validateTrelloBoards(mappedTrelloBoards)).thenReturn(mappedTrelloBoards);

        //When
        List<TrelloBoardDto> fetchedTrelloBoardDtos = trelloFacade.fetchTrelloBoards();

        //Then
        assertNotNull(fetchedTrelloBoardDtos);
        assertEquals(2, fetchedTrelloBoardDtos.size());

        AtomicInteger boardNum = new AtomicInteger(0);
        AtomicInteger listNum = new AtomicInteger(0);
        trelloBoardDtos.forEach(trelloBoardDto -> {
            boardNum.getAndIncrement();
            assertEquals(boardNum.toString(), trelloBoardDto.getId());
            assertEquals("Trello Board ".concat(boardNum.toString()), trelloBoardDto.getName());

            listNum.getAndSet(0);
            trelloBoardDto.getLists().forEach(trelloListDto -> {
                listNum.getAndIncrement();
                assertEquals(listNum.toString(), trelloListDto.getId());
                assertEquals("Trello List ".concat(listNum.toString()), trelloListDto.getName());
                assertEquals(false, trelloListDto.isClosed());
            });
        });
    }
}
