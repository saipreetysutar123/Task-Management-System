package com.saipreety.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saipreety.taskmanagement.dto.UserRequestDTO;
import com.saipreety.taskmanagement.dto.UserResponseDTO;
import com.saipreety.taskmanagement.entity.Role;
import com.saipreety.taskmanagement.security.CustomUserDetailsService;
import com.saipreety.taskmanagement.service.JwtService;
import com.saipreety.taskmanagement.service.UserService;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService service;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    void createUser_ShouldReturn201Created() throws Exception {

        // Arrange
        UserRequestDTO request = new UserRequestDTO();
        request.setFullName("Saipreety Sutar");
        request.setEmail("saipreety@example.com");
        request.setPassword("password123");

        UserResponseDTO response = new UserResponseDTO();
        response.setId(1L);
        response.setFullName("Saipreety Sutar");
        response.setEmail("saipreety@example.com");
        response.setRole(Role.USER);

        when(service.createUser(any(UserRequestDTO.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                        post("/user/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName")
                        .value("Saipreety Sutar"))
                .andExpect(jsonPath("$.email")
                        .value("saipreety@example.com"))
                .andExpect(jsonPath("$.role")
                        .value("USER"));
    }

    @Test
    void getAllUsers_ShouldReturn200Ok() throws Exception {

        // Arrange
        UserResponseDTO user1 = new UserResponseDTO();
        user1.setId(1L);
        user1.setFullName("Saipreety Sutar");
        user1.setEmail("saipreety@example.com");
        user1.setRole(Role.USER);

        UserResponseDTO user2 = new UserResponseDTO();
        user2.setId(2L);
        user2.setFullName("Test User");
        user2.setEmail("test@example.com");
        user2.setRole(Role.USER);

        when(service.getAllUsers())
                .thenReturn(List.of(user1, user2));

        // Act & Assert
        mockMvc.perform(
                        get("/user/fetchAll")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName")
                        .value("Saipreety Sutar"))
                .andExpect(jsonPath("$[0].email")
                        .value("saipreety@example.com"))
                .andExpect(jsonPath("$[0].role")
                        .value("USER"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].fullName")
                        .value("Test User"))
                .andExpect(jsonPath("$[1].email")
                        .value("test@example.com"));
    }

    @Test
    void getById_ShouldReturn200Ok() throws Exception {

        // Arrange
        UserResponseDTO response = new UserResponseDTO();
        response.setId(1L);
        response.setFullName("Saipreety Sutar");
        response.setEmail("saipreety@example.com");
        response.setRole(Role.USER);

        when(service.getUserById(1L))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                        get("/user/fetch/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName")
                        .value("Saipreety Sutar"))
                .andExpect(jsonPath("$.email")
                        .value("saipreety@example.com"))
                .andExpect(jsonPath("$.role")
                        .value("USER"));
    }

    @Test
    void getById_ShouldReturn404WhenUserNotFound() throws Exception {

        // Arrange
        when(service.getUserById(999L))
                .thenReturn(null);

        // Act & Assert
        mockMvc.perform(
                        get("/user/fetch/999")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_ShouldReturn200Ok() throws Exception {

        // Arrange
        UserRequestDTO request = new UserRequestDTO();
        request.setFullName("Updated Saipreety");
        request.setEmail("updated@example.com");
        request.setPassword("password123");

        UserResponseDTO response = new UserResponseDTO();
        response.setId(1L);
        response.setFullName("Updated Saipreety");
        response.setEmail("updated@example.com");
        response.setRole(Role.USER);

        when(service.updateUser(any(Long.class), any(UserRequestDTO.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                        put("/user/update/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName")
                        .value("Updated Saipreety"))
                .andExpect(jsonPath("$.email")
                        .value("updated@example.com"))
                .andExpect(jsonPath("$.role")
                        .value("USER"));
    }

    @Test
    void updateUser_ShouldReturn404WhenUserNotFound() throws Exception {

        // Arrange
        UserRequestDTO request = new UserRequestDTO();
        request.setFullName("Updated Saipreety");
        request.setEmail("updated@example.com");
        request.setPassword("password123");

        when(service.updateUser(any(Long.class), any(UserRequestDTO.class)))
                .thenReturn(null);

        // Act & Assert
        mockMvc.perform(
                        put("/user/update/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_ShouldReturn200Ok() throws Exception {

        // Arrange
        when(service.deleteUser(1L))
                .thenReturn(ResponseEntity.ok("User deleted successfully"));

        // Act & Assert
        mockMvc.perform(
                        delete("/user/delete/1")
                )
                .andExpect(status().isOk());
    }
}