package com.crud.tasks.service;

import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DbServiceTest {
    @InjectMocks
    private DbService dbService;

    @Mock
    private TaskRepository repository;

    @Test
    public void getAllTasksTest() {
        // Given
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1L, "title1", "content1"));
        taskList.add(new Task(2L, "title2", "content2"));
        when(dbService.getAllTasks()).thenReturn(taskList);

        // When
        List<Task> retrievedTaskList = dbService.getAllTasks();

        // Then
        assertEquals(2, retrievedTaskList.size());

        assertEquals((Long)1L, retrievedTaskList.get(0).getId());
        assertEquals("title1", retrievedTaskList.get(0).getTitle());
        assertEquals("content1", retrievedTaskList.get(0).getContent());

        assertEquals((Long)2L, retrievedTaskList.get(1).getId());
        assertEquals("title2", retrievedTaskList.get(1).getTitle());
        assertEquals("content2", retrievedTaskList.get(1).getContent());
    }

    @Test
    public void getTaskTest() {
        // Given
        Task task = new Task(1L, "title", "content");
        when(dbService.getTask(1L)).thenReturn(java.util.Optional.of(task));

        // When
        Optional<Task> retrievedTask = dbService.getTask(1L);

        // Then
        assertEquals((Long)1L, retrievedTask.get().getId());
        assertEquals("title", retrievedTask.get().getTitle());
        assertEquals("content", retrievedTask.get().getContent());
    }

    @Test
    public void getNoTaskTest() {
        // Given
        when(dbService.getTask(1L)).thenReturn(null);

        // When
        Optional<Task> expectedTask = dbService.getTask(1L);

        // Then
        assertNull(expectedTask);
    }

    @Test
    public void saveTaskTest() {
        // Given
        Task task = new Task(1L, "title", "content");
        when(dbService.saveTask(task)).thenReturn(task);

        // When
        Task savedTask = dbService.saveTask(task);

        // Then
        assertEquals((Long)1L, savedTask.getId());
        assertEquals("title", savedTask.getTitle());
        assertEquals("content", savedTask.getContent());
    }
}
