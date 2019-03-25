package com.crud.tasks.trello.facade;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TaskMapper;
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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrelloFacadeTest {
    @InjectMocks
    private TrelloFacade trelloFacade;

    @InjectMocks
    private TaskMapper taskMapper;

    @Mock
    private TrelloService trelloService;

    @Mock
    private TrelloValidator trelloValidator;

    @Mock
    private TrelloMapper trelloMapper;

    @Test
    public void testMapToTask() {
        //Given
        TaskDto taskDto = new TaskDto(1L, "Task", "Content");

        //When
        Task task = taskMapper.mapToTask(taskDto);

        //Then
        assertEquals((Long)1L, task.getId());
        assertEquals("Task", task.getTitle());
        assertEquals("Content", task.getContent());
    }

    @Test
    public void testMapToTaskDto() {
        //Given
        Task task = new Task(1L, "Task", "Content");

        //When
        TaskDto taskDto = taskMapper.mapToTaskDto(task);

        //Then
        assertEquals((Long)1L, taskDto.getId());
        assertEquals("Task", taskDto.getTitle());
        assertEquals("Content", taskDto.getContent());
    }

    @Test
    public void testMapToTaskListDto() {
        //Given
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1L, "Task 1", "Content 1"));
        taskList.add(new Task(2L, "Task 2", "Content 2"));

        //When
        List<TaskDto> taskListDto = taskMapper.mapToTaskListDto(taskList);
        taskList.remove(taskList.get(1));

        //Then
        assertEquals(2, taskListDto.size());
        assertEquals((Long)1L, taskListDto.get(0).getId());
        assertEquals("Task 1", taskListDto.get(0).getTitle());
        assertEquals("Content 1", taskListDto.get(0).getContent());
    }

    @Test
    public void shouldFetchEmptyTrelloBoards() {
        //Given
        List<TrelloListDto> trelloListsDto = new ArrayList<>();
        trelloListsDto.add(new TrelloListDto("1", "Trello List 1", false));
        trelloListsDto.add(new TrelloListDto("2", "Trello List 2", false));

        List<TrelloBoardDto> trelloBoardsDto = new ArrayList<>();
        trelloBoardsDto.add(new TrelloBoardDto("1", "Trello Board 1", trelloListsDto));
        trelloBoardsDto.add(new TrelloBoardDto("2", "Trello Board 2", trelloListsDto));

        List<TrelloList> mappedTrelloLists = new ArrayList<>();
        mappedTrelloLists.add(new TrelloList("1", "Trello List 1", false));
        mappedTrelloLists.add(new TrelloList("2", "Trello List 2", false));

        List<TrelloBoard> mappedTrelloBoards = new ArrayList<>();
        mappedTrelloBoards.add(new TrelloBoard("1", "Trello Board 1", mappedTrelloLists));
        mappedTrelloBoards.add(new TrelloBoard("2", "Trello Board 2", mappedTrelloLists));

        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoardsDto);
        when(trelloMapper.mapToBoards(trelloBoardsDto)).thenReturn(mappedTrelloBoards);
        when(trelloMapper.mapToBoardsDto(anyList())).thenReturn(new ArrayList<>());
        when(trelloValidator.validateTrelloBoards(mappedTrelloBoards)).thenReturn(new ArrayList<>());

        //When
        List<TrelloBoardDto> fetchedTrelloBoardsDto = trelloFacade.fetchTrelloBoards();

        //Then
        assertNotNull(fetchedTrelloBoardsDto);
        assertEquals(0, fetchedTrelloBoardsDto.size());
    }

    @Test
    public void shouldFetchTrelloBoards() {
        //Given
        List<TrelloListDto> trelloListsDto = new ArrayList<>();
        trelloListsDto.add(new TrelloListDto("1", "Trello List 1", false));
        trelloListsDto.add(new TrelloListDto("2", "Trello List 2", false));

        List<TrelloBoardDto> trelloBoardsDto = new ArrayList<>();
        trelloBoardsDto.add(new TrelloBoardDto("1", "Trello Board 1", trelloListsDto));
        trelloBoardsDto.add(new TrelloBoardDto("2", "Trello Board 2", trelloListsDto));

        List<TrelloList> mappedTrelloLists = new ArrayList<>();
        mappedTrelloLists.add(new TrelloList("1", "Trello List 1", false));
        mappedTrelloLists.add(new TrelloList("2", "Trello List 2", false));

        List<TrelloBoard> mappedTrelloBoards = new ArrayList<>();
        mappedTrelloBoards.add(new TrelloBoard("1", "Trello Board 1", mappedTrelloLists));
        mappedTrelloBoards.add(new TrelloBoard("2", "Trello Board 2", mappedTrelloLists));

        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoardsDto);
        when(trelloMapper.mapToBoards(trelloBoardsDto)).thenReturn(mappedTrelloBoards);
        when(trelloMapper.mapToBoardsDto(anyList())).thenReturn(trelloBoardsDto);
        when(trelloValidator.validateTrelloBoards(mappedTrelloBoards)).thenReturn(mappedTrelloBoards);

        //When
        List<TrelloBoardDto> fetchedTrelloBoardsDto = trelloFacade.fetchTrelloBoards();

        //Then
        assertNotNull(fetchedTrelloBoardsDto);
        assertEquals(2, fetchedTrelloBoardsDto.size());

        AtomicInteger boardNum = new AtomicInteger(0);
        AtomicInteger listNum = new AtomicInteger(0);
        trelloBoardsDto.forEach(trelloBoardDto -> {
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

    @Test
    public void shouldFetchCreatedTrelloCardDto() {
        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("Card", "Trello Card", "1", "1");
        Trello trello = new Trello(1, 1);
        AttachmentsByType attachmentsByType = new AttachmentsByType(trello);
        TrelloBadgeDto trelloBadgeDto = new TrelloBadgeDto(3, attachmentsByType);
        CreatedTrelloCardDto createdTrelloCardDto =
                new CreatedTrelloCardDto("id", "name", "URL", trelloBadgeDto);

        when(trelloFacade.createCard(trelloCardDto)).thenReturn(createdTrelloCardDto);

        //When
        CreatedTrelloCardDto fetchedTrelloCardDto = trelloFacade.createCard(trelloCardDto);

        //Then
        assertEquals("id", fetchedTrelloCardDto.getId());
        assertEquals("name", fetchedTrelloCardDto.getName());
        assertEquals("URL", fetchedTrelloCardDto.getShortUrl());
        assertEquals(trelloBadgeDto, fetchedTrelloCardDto.getBadges());
    }
}
