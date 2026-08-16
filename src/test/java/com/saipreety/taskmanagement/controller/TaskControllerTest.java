package com.saipreety.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.saipreety.taskmanagement.dto.TaskRequestDTO;
import com.saipreety.taskmanagement.dto.TaskResponseDTO;
import com.saipreety.taskmanagement.entity.TaskPriority;
import com.saipreety.taskmanagement.entity.TaskStatus;
import com.saipreety.taskmanagement.security.CustomUserDetailsService;
import com.saipreety.taskmanagement.service.JwtService;
import com.saipreety.taskmanagement.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService service;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createTask_ShouldReturn201Created() throws Exception {

        TaskRequestDTO request = new TaskRequestDTO();

        request.setTitle("Complete Backend");
        request.setDescription("Complete Spring Boot backend project");
        request.setStatus(TaskStatus.TODO);
        request.setPriority(TaskPriority.HIGH);
        request.setDueDate(LocalDate.of(2026, 8, 30));
        request.setUserId(1L);
        request.setProjectId(1L);


        TaskResponseDTO response = new TaskResponseDTO();

        response.setId(1L);
        response.setTitle("Complete Backend");
        response.setDescription("Complete Spring Boot backend project");
        response.setStatus(TaskStatus.TODO);
        response.setPriority(TaskPriority.HIGH);
        response.setDueDate(LocalDate.of(2026, 8, 30));
        response.setUserId(1L);
        response.setProjectId(1L);


        when(service.createTask(any(TaskRequestDTO.class)))
                .thenReturn(response);


        mockMvc.perform(
                        post("/task/create")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Complete Backend"))
                .andExpect(jsonPath("$.description")
                        .value("Complete Spring Boot backend project"))
                .andExpect(jsonPath("$.status")
                        .value("TODO"))
                .andExpect(jsonPath("$.priority")
                        .value("HIGH"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.projectId").value(1));
    }


    @Test
    void getAllTasks_ShouldReturn200Ok() throws Exception {

        TaskResponseDTO task1 = new TaskResponseDTO();

        task1.setId(1L);
        task1.setTitle("Task One");
        task1.setDescription("First task description");
        task1.setStatus(TaskStatus.TODO);
        task1.setPriority(TaskPriority.HIGH);
        task1.setDueDate(LocalDate.of(2026, 8, 20));
        task1.setUserId(1L);
        task1.setProjectId(1L);


        TaskResponseDTO task2 = new TaskResponseDTO();

        task2.setId(2L);
        task2.setTitle("Task Two");
        task2.setDescription("Second task description");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setPriority(TaskPriority.MEDIUM);
        task2.setDueDate(LocalDate.of(2026, 8, 25));
        task2.setUserId(2L);
        task2.setProjectId(1L);


        Page<TaskResponseDTO> page =
                new PageImpl<>(Arrays.asList(task1, task2));


        when(service.getAllTasks(0, 5, "id", "asc"))
                .thenReturn(page);


        mockMvc.perform(
                        get("/task/fetchAll")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sortBy", "id")
                                .param("direction", "asc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Task One"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title")
                        .value("Task Two"));
    }


    @Test
    void getTaskById_ShouldReturn200Ok() throws Exception {

        TaskResponseDTO response = new TaskResponseDTO();

        response.setId(1L);
        response.setTitle("Complete Backend");
        response.setDescription("Complete Spring Boot backend project");
        response.setStatus(TaskStatus.TODO);
        response.setPriority(TaskPriority.HIGH);
        response.setDueDate(LocalDate.of(2026, 8, 30));
        response.setUserId(1L);
        response.setProjectId(1L);


        when(service.getTaskById(1L))
                .thenReturn(response);


        mockMvc.perform(
                        get("/task/fetch/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Complete Backend"))
                .andExpect(jsonPath("$.description")
                        .value("Complete Spring Boot backend project"));
    }


    @Test
    void updateTask_ShouldReturn200Ok() throws Exception {

        TaskRequestDTO request = new TaskRequestDTO();

        request.setTitle("Updated Task");
        request.setDescription("Updated task description");
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setPriority(TaskPriority.MEDIUM);
        request.setDueDate(LocalDate.of(2026, 9, 1));
        request.setUserId(1L);
        request.setProjectId(1L);


        TaskResponseDTO response = new TaskResponseDTO();

        response.setId(1L);
        response.setTitle("Updated Task");
        response.setDescription("Updated task description");
        response.setStatus(TaskStatus.IN_PROGRESS);
        response.setPriority(TaskPriority.MEDIUM);
        response.setDueDate(LocalDate.of(2026, 9, 1));
        response.setUserId(1L);
        response.setProjectId(1L);


        when(service.updateTask(
                any(TaskRequestDTO.class),
                any(Long.class)
        )).thenReturn(response);


        mockMvc.perform(
                        put("/task/update/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Updated Task"))
                .andExpect(jsonPath("$.description")
                        .value("Updated task description"));
    }


    @Test
    void deleteTask_ShouldReturn200Ok() throws Exception {

        doNothing().when(service).deleteTask(1L);


        mockMvc.perform(
                        delete("/task/delete/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$")
                                .value("Task deleted successfully")
                );
    }


    @Test
    void getTasksByStatus_ShouldReturn200Ok() throws Exception {

        TaskResponseDTO task = new TaskResponseDTO();

        task.setId(1L);
        task.setTitle("Todo Task");
        task.setDescription("Task with todo status");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setUserId(1L);
        task.setProjectId(1L);


        Page<TaskResponseDTO> page =
                new PageImpl<>(Arrays.asList(task));


        when(service.getTasksByStatus(
                TaskStatus.TODO,
                0,
                5,
                "id",
                "asc"
        )).thenReturn(page);


        mockMvc.perform(
                        get("/task/fetch/status/TODO")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sortBy", "id")
                                .param("direction", "asc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].status")
                        .value("TODO"));
    }


    @Test
    void getTasksByPriority_ShouldReturn200Ok() throws Exception {

        TaskResponseDTO task = new TaskResponseDTO();

        task.setId(1L);
        task.setTitle("Important Task");
        task.setDescription("High priority task");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setUserId(1L);
        task.setProjectId(1L);


        Page<TaskResponseDTO> page =
                new PageImpl<>(Arrays.asList(task));


        when(service.getTasksByPriority(
                TaskPriority.HIGH,
                0,
                5,
                "id",
                "asc"
        )).thenReturn(page);


        mockMvc.perform(
                        get("/task/fetch/priority/HIGH")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sortBy", "id")
                                .param("direction", "asc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].priority")
                        .value("HIGH"));
    }


    @Test
    void getTasksByProjectId_ShouldReturn200Ok() throws Exception {

        TaskResponseDTO task = new TaskResponseDTO();

        task.setId(1L);
        task.setTitle("Project Task");
        task.setDescription("Task belonging to project");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        task.setUserId(1L);
        task.setProjectId(10L);


        Page<TaskResponseDTO> page =
                new PageImpl<>(Arrays.asList(task));


        when(service.getTasksByProjectId(
                10L,
                0,
                5,
                "id",
                "asc"
        )).thenReturn(page);


        mockMvc.perform(
                        get("/task/fetch/project/10")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sortBy", "id")
                                .param("direction", "asc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].projectId")
                        .value(10));
    }


    @Test
    void getTasksByUser_ShouldReturn200Ok() throws Exception {

        TaskResponseDTO task = new TaskResponseDTO();

        task.setId(1L);
        task.setTitle("User Task");
        task.setDescription("Task assigned to user");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(TaskPriority.MEDIUM);
        task.setUserId(5L);
        task.setProjectId(1L);


        Page<TaskResponseDTO> page =
                new PageImpl<>(Arrays.asList(task));


        when(service.getTasksByUser(
                5L,
                0,
                5,
                "id",
                "asc"
        )).thenReturn(page);


        mockMvc.perform(
                        get("/task/fetch/user/5")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sortBy", "id")
                                .param("direction", "asc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].userId")
                        .value(5));
    }


    @Test
    void searchTasks_ShouldReturn200Ok() throws Exception {

        TaskResponseDTO task = new TaskResponseDTO();

        task.setId(1L);
        task.setTitle("Spring Boot Task");
        task.setDescription("Task related to Spring Boot");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setUserId(1L);
        task.setProjectId(1L);


        Page<TaskResponseDTO> page =
                new PageImpl<>(Arrays.asList(task));


        when(service.searchTasks(
                "Spring",
                0,
                5,
                "id",
                "asc"
        )).thenReturn(page);


        mockMvc.perform(
                        get("/task/search")
                                .param("keyword", "Spring")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sortBy", "id")
                                .param("direction", "asc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Spring Boot Task"));
    }
}