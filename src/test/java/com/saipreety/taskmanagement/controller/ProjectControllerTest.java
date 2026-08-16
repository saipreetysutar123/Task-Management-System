package com.saipreety.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saipreety.taskmanagement.dto.ProjectRequestDTO;
import com.saipreety.taskmanagement.dto.ProjectResponseDTO;
import com.saipreety.taskmanagement.security.CustomAccessDeniedHandler;
import com.saipreety.taskmanagement.security.CustomAuthenticationEntryPoint;
import com.saipreety.taskmanagement.security.CustomUserDetailsService;
import com.saipreety.taskmanagement.security.JwtAuthenticationFilter;
import com.saipreety.taskmanagement.service.JwtService;
import com.saipreety.taskmanagement.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
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

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProjectService service;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @MockitoBean
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Test
    void createProject_ShouldReturn201Created() throws Exception {

        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("Task Management System");
        request.setDescription("Backend project using Spring Boot");

        ProjectResponseDTO response = new ProjectResponseDTO();
        response.setId(1L);
        response.setName("Task Management System");
        response.setDescription("Backend project using Spring Boot");

        when(service.createProject(any(ProjectRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/project/create")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name")
                        .value("Task Management System"))
                .andExpect(jsonPath("$.description")
                        .value("Backend project using Spring Boot"));
    }


    @Test
    void getAllProjects_ShouldReturn200Ok() throws Exception {

        ProjectResponseDTO project1 = new ProjectResponseDTO();
        project1.setId(1L);
        project1.setName("Task Management System");
        project1.setDescription("Backend project");

        ProjectResponseDTO project2 = new ProjectResponseDTO();
        project2.setId(2L);
        project2.setName("LMS");
        project2.setDescription("Learning Management System");

        when(service.getAllProjects())
                .thenReturn(Arrays.asList(project1, project2));

        mockMvc.perform(
                        get("/project/fetchAll")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name")
                        .value("Task Management System"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name")
                        .value("LMS"));
    }


    @Test
    void getProjectById_ShouldReturn200Ok() throws Exception {

        ProjectResponseDTO response = new ProjectResponseDTO();
        response.setId(1L);
        response.setName("Task Management System");
        response.setDescription("Backend project");

        when(service.getProjectById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/project/fetch/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name")
                        .value("Task Management System"))
                .andExpect(jsonPath("$.description")
                        .value("Backend project"));
    }


    @Test
    void updateProject_ShouldReturn200Ok() throws Exception {

        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("Updated Project");
        request.setDescription("Updated description");

        ProjectResponseDTO response = new ProjectResponseDTO();
        response.setId(1L);
        response.setName("Updated Project");
        response.setDescription("Updated description");

        when(service.updateProject(
                any(ProjectRequestDTO.class),
                any(Long.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/project/update/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name")
                        .value("Updated Project"))
                .andExpect(jsonPath("$.description")
                        .value("Updated description"));
    }


    @Test
    void deleteProject_ShouldReturn200Ok() throws Exception {

        doNothing().when(service).deleteProject(1L);

        mockMvc.perform(
                        delete("/project/delete/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$")
                                .value("Project deleted successfully")
                );
    }
}
