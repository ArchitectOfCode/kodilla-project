package com.crud.tasks.mapper;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TaskMapperTest {
    @InjectMocks
    private TaskMapper taskMapper;

    @Test
    public void mapToTaskTest() {
        // Given
        TaskDto taskDto = new TaskDto(1L, "title", "content");

        // When
        Task task = taskMapper.mapToTask(taskDto);

        // Then
        assertEquals((Long)1L, task.getId());
        assertEquals("title", task.getTitle());
        assertEquals("content", task.getContent());
    }

    @Test
    public void mapToTaskDtoTest() {
        // Given
        Task task = new Task(1L, "title", "content");

        // When
        TaskDto taskDto = taskMapper.mapToTaskDto(task);

        // Then
        assertEquals((Long)1L, taskDto.getId());
        assertEquals("title", taskDto.getTitle());
        assertEquals("content", taskDto.getContent());
    }

    @Test
    public void mapToTaskDtoListTest() {
        // Given
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1L, "title1", "content1"));
        taskList.add(new Task(2L, "title2", "content2"));

        // When
        List<TaskDto> taskDtoList = taskMapper.mapToTaskDtoList(taskList);

        // Then
        assertEquals(2, taskDtoList.size());

        assertEquals((Long)1L, taskDtoList.get(0).getId());
        assertEquals("title1", taskDtoList.get(0).getTitle());
        assertEquals("content1", taskDtoList.get(0).getContent());

        assertEquals((Long)2L, taskDtoList.get(1).getId());
        assertEquals("title2", taskDtoList.get(1).getTitle());
        assertEquals("content2", taskDtoList.get(1).getContent());
    }
}
