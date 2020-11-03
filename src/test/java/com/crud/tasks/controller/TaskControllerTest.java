package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskContoller.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService dbService;

    @MockBean
    private TaskMapper taskMapper;

    @Test
    public void shouldFetchAllTasks() throws Exception {
        // Given
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1L, "Test task", "Test content"));

        List<TaskDto> taskListDto = new ArrayList<>();
        taskListDto.add(new TaskDto(1L, "Test task", "Test content"));

        when(taskMapper.mapToTaskDtoList(ArgumentMatchers.anyList())).thenReturn(taskListDto);
        when(dbService.getAllTasks()).thenReturn(taskList);

        // When & Then
        mockMvc.perform(get("/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test task")))
                .andExpect(jsonPath("$[0].content", is("Test content")));
    }

    @Test
    public void shouldFetchTask() throws Exception {
        // Given
        Task task = new Task(1L, "Test task", "Test content");
        TaskDto taskDto = new TaskDto(1L, "Test task", "Test content");

        when(taskMapper.mapToTaskDto(ArgumentMatchers.any(Task.class))).thenReturn(taskDto);
        when(dbService.getTaskById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(task));

        // When & Then
        mockMvc.perform(get("/v1/tasks/{taskId}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test task")))
                .andExpect(jsonPath("$.content", is("Test content")));
    }

    @Test
    public void shouldDeleteTask() throws Exception {
        // Given
        Task task = new Task(1L, "Test task", "Test content");

        when(dbService.getTaskById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(task));

        // When & Then
        mockMvc.perform(delete("/v1/tasks/{taskId}", "1"))
                .andExpect(status().isOk());
        verify(dbService, times(1)).deleteTaskByID(1L);
    }

//    @Test
//    public void shouldUpdateTask() throws Exception {
//        // Given
//        Task task = new Task(1L, "Test task", "Test content");
//        TaskDto taskDto = new TaskDto(1L, "Test task", "Test content");
//
//        when(taskMapper.mapToTaskDto(ArgumentMatchers.any(Task.class))).thenReturn(taskDto);
//        when(taskMapper.mapToTask(ArgumentMatchers.any(TaskDto.class))).thenReturn(task);
//        when(dbService.saveTask(ArgumentMatchers.any(Task.class))).thenReturn(task);
//
//        Gson gson = new Gson();
//        String jsonContent = gson.toJson(taskDto);
//
//        // When & Then
//        mockMvc.perform(put("/v1/tasks")
//                .characterEncoding("UTF-8")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonContent))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.title", is("Test task")))
//                .andExpect(jsonPath("$.content", is("Test content")));
//    }
//
//    @Test
//    public void shouldCreateTask() throws Exception {
//        // Given
//        Task task = new Task(1L, "Test task", "Test content");
//        TaskDto taskDto = new TaskDto(1L, "Test task", "Test content");
//
//        when(taskMapper.mapToTask(ArgumentMatchers.any(TaskDto.class))).thenReturn(task);
//
//        Gson gson = new Gson();
//        String jsonContent = gson.toJson(taskDto);
//
//        // When & Then
//        mockMvc.perform(post("/v1/tasks")
//                .characterEncoding("UTF-8")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonContent))
//                .andExpect(status().isOk());
//        verify(dbService, times(1)).saveTask(ArgumentMatchers.any(Task.class));
//    }

}
