package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService dbService;

    @MockBean
    private TaskMapper taskMapper;

    @Test
    public void shouldFetchEmptyListOfTasks() throws Exception {
        // Given
        List<Task> trelloTasks = new ArrayList<>();
        List<TaskDto> trelloTasksDto = new ArrayList<>();

        when(taskMapper.mapToTaskListDto(trelloTasks)).thenReturn(trelloTasksDto);
        when(dbService.getAllTasks()).thenReturn(trelloTasks);

        // When & Then
        mockMvc.perform(get("/v1/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))  // or isOk()
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldFetchTwoTasks() throws Exception {
        // Given
        List<Task> trelloTasks = new ArrayList<>();
        trelloTasks.add(new Task(1L, "Task title 1", "Task content 1"));
        trelloTasks.add(new Task(2L, "Task title 2", "Task content 2"));

        List<TaskDto> trelloTasksDto = new ArrayList<>();
        trelloTasksDto.add(new TaskDto(1L, "Task title 1", "Task content 1"));
        trelloTasksDto.add(new TaskDto(2L, "Task title 2", "Task content 2"));

        when(taskMapper.mapToTaskListDto(trelloTasks)).thenReturn(trelloTasksDto);
        when(dbService.getAllTasks()).thenReturn(trelloTasks);

        // When & Then
        /*AtomicInteger taskNum = new AtomicInteger(0);
        mockMvc.perform(get("/v1/task/getTasks").contentType(MediaType.APPLICATION_JSON))
                .taskNum.getAndIncrement()
                .andExpect(status().is(200))  // or isOk()
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[taskNum].id", is(taskNum.longValue())))
                .andExpect(jsonPath("$[taskNum].title", is("Task title ".concat(taskNum.toString()))));*/

        mockMvc.perform(get("/v1/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))  // or isOk()
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is((int)1L)))
                .andExpect(jsonPath("$[0].title", is("Task title 1")))
                .andExpect(jsonPath("$[0].content", is("Task content 1")))
                .andExpect(jsonPath("$[1].id", is((int)2L)))
                .andExpect(jsonPath("$[1].title", is("Task title 2")))
                .andExpect(jsonPath("$[1].content", is("Task content 2")));
    }

    @Test
    public void shouldGetSecondTask() throws Exception {
        // Given
        List<Task> trelloTasks = new ArrayList<>();
        Task task1 = new Task(1L, "Task title 1", "Task content 1");
        Task task2 = new Task(2L, "Task title 2", "Task content 2");
        trelloTasks.add(task1);
        trelloTasks.add(task2);

        List<TaskDto> trelloTasksDto = new ArrayList<>();
        TaskDto taskDto1 = new TaskDto(1L, "Task title 1", "Task content 1");
        TaskDto taskDto2 = new TaskDto(2L, "Task title 2", "Task content 2");
        trelloTasksDto.add(taskDto1);
        trelloTasksDto.add(taskDto2);

        when(taskMapper.mapToTaskDto(task2)).thenReturn(taskDto2);
        when(dbService.getTask(2L)).thenReturn(java.util.Optional.of(task2));

        // When & Then
        mockMvc.perform(get("/v1/tasks/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int)2L)))
                .andExpect(jsonPath("$.title", is("Task title 2")))
                .andExpect(jsonPath("$.content", is("Task content 2")));
    }

    @Test
    public void shouldDeleteFirstTask() throws Exception {
        // Given
        Task task = new Task(1L, "Task title", "Task content");
        when(dbService.getTask(task.getId())).thenReturn(Optional.ofNullable(task));

        // When & Then
        mockMvc.perform(delete("/v1/tasks/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateSecondTask() throws Exception {
        // Given
        Task modifiedTask = new Task(1L, "Modified task title", "Modified task content");
        TaskDto modifiedTaskDto = new TaskDto(1L, "Modified task title", "Modified task content");

        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(modifiedTask);
        when(taskMapper.mapToTaskDto(any(Task.class))).thenReturn(modifiedTaskDto);
        when(dbService.saveTask(any(Task.class))).thenReturn(modifiedTask);

        Gson gson =new Gson();
        String jsonContent = gson.toJson(modifiedTaskDto);

        // When & Then
        mockMvc.perform(put("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int)1L)))
                .andExpect(jsonPath("$.title", is("Modified task title")))
                .andExpect(jsonPath("$.content", is("Modified task content")));
    }

    @Test
    public void shouldCreateTask() throws Exception {
        // Given
        Task newTask = new Task(1L, "New task title", "New task content");
        TaskDto newTaskDto = new TaskDto(1L, "New task title", "New task content");

        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(newTask);
        when(taskMapper.mapToTaskDto(any(Task.class))).thenReturn(newTaskDto);
        when(dbService.saveTask(any(Task.class))).thenReturn(newTask);

        Gson gson =new Gson();
        String jsonContent = gson.toJson(newTaskDto);

        // When & Then
        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
    }
}
