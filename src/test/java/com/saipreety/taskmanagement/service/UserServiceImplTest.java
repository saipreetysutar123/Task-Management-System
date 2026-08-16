package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.entity.Role;
import com.saipreety.taskmanagement.entity.UserEntity;
import com.saipreety.taskmanagement.exception.UserNotFoundException;
import com.saipreety.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.saipreety.taskmanagement.dto.UserRequestDTO;
import com.saipreety.taskmanagement.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ShouldCreateAndReturnUser() {

        // Arrange
        UserRequestDTO request = new UserRequestDTO();
        request.setFullName("Saipreety Sutar");
        request.setEmail("saipreety@example.com");
        request.setPassword("password123");

        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        savedUser.setFullName("Saipreety Sutar");
        savedUser.setEmail("saipreety@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.USER);
        savedUser.setCreatedAt(LocalDateTime.now());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(repository.save(any(UserEntity.class)))
                .thenReturn(savedUser);

        // Act
        UserResponseDTO result =
                userService.createUser(request);

        // Assert
        assertNotNull(result);

        assertEquals(1L, result.getId());
        assertEquals(
                "Saipreety Sutar",
                result.getFullName()
        );
        assertEquals(
                "saipreety@example.com",
                result.getEmail()
        );
        assertEquals(
                Role.USER,
                result.getRole()
        );

        assertNotNull(result.getCreatedAt());

        verify(passwordEncoder).encode("password123");
        verify(repository).save(any(UserEntity.class));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {

        // Arrange
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setFullName("Saipreety Sutar");
        user1.setEmail("saipreety@example.com");
        user1.setRole(Role.USER);
        user1.setCreatedAt(LocalDateTime.now());

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setFullName("John Doe");
        user2.setEmail("john@example.com");
        user2.setRole(Role.USER);
        user2.setCreatedAt(LocalDateTime.now());

        when(repository.findAll())
                .thenReturn(List.of(user1, user2));

        // Act
        List<UserResponseDTO> result =
                userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(
                "Saipreety Sutar",
                result.get(0).getFullName()
        );

        assertEquals(
                "John Doe",
                result.get(1).getFullName()
        );

        verify(repository).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {

        // Arrange
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setFullName("Saipreety Sutar");
        user.setEmail("saipreety@example.com");
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        when(repository.findById(userId))
                .thenReturn(Optional.of(user));

        // Act
        UserResponseDTO result =
                userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(
                "Saipreety Sutar",
                result.getFullName()
        );
        assertEquals(
                "saipreety@example.com",
                result.getEmail()
        );
        assertEquals(
                Role.USER,
                result.getRole()
        );

        verify(repository).findById(userId);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowException() {

        // Arrange
        Long userId = 99L;

        when(repository.findById(userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(userId)
        );

        assertEquals(
                "User not found with id: 99",
                exception.getMessage()
        );

        verify(repository).findById(userId);
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateUser() {

        // Arrange
        Long userId = 1L;

        UserRequestDTO request = new UserRequestDTO();
        request.setFullName("Updated Name");
        request.setEmail("updated@example.com");
        request.setPassword("newPassword123");

        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);
        existingUser.setFullName("Old Name");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldEncodedPassword");
        existingUser.setRole(Role.USER);
        existingUser.setCreatedAt(LocalDateTime.now());

        UserEntity savedUser = existingUser;
        savedUser.setFullName("Updated Name");
        savedUser.setEmail("updated@example.com");
        savedUser.setPassword("newEncodedPassword");

        when(repository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(passwordEncoder.encode("newPassword123"))
                .thenReturn("newEncodedPassword");

        when(repository.save(existingUser))
                .thenReturn(savedUser);

        // Act
        UserResponseDTO result =
                userService.updateUser(userId, request);

        // Assert
        assertNotNull(result);

        assertEquals(
                userId,
                result.getId()
        );

        assertEquals(
                "Updated Name",
                result.getFullName()
        );

        assertEquals(
                "updated@example.com",
                result.getEmail()
        );

        assertEquals(
                Role.USER,
                result.getRole()
        );

        verify(repository).findById(userId);
        verify(passwordEncoder).encode("newPassword123");
        verify(repository).save(existingUser);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException() {

        // Arrange
        Long userId = 99L;

        UserRequestDTO request = new UserRequestDTO();
        request.setFullName("Updated Name");
        request.setEmail("updated@example.com");
        request.setPassword("newPassword123");

        when(repository.findById(userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(userId, request)
        );

        assertEquals(
                "User not found with id: 99",
                exception.getMessage()
        );

        verify(repository).findById(userId);

        // Password encoding and save should never happen
        verify(passwordEncoder, never()).encode(anyString());
        verify(repository, never()).save(any(UserEntity.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {

        // Arrange
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);

        when(repository.findById(userId))
                .thenReturn(Optional.of(user));

        // Act
        ResponseEntity<Object> result =
                userService.deleteUser(userId);

        // Assert
        assertNotNull(result);

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertEquals(
                "User deleted successfully",
                result.getBody()
        );

        verify(repository).findById(userId);
        verify(repository).deleteById(userId);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowException() {

        // Arrange
        Long userId = 99L;

        when(repository.findById(userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(userId)
        );

        assertEquals(
                "User not found with id: 99",
                exception.getMessage()
        );

        verify(repository).findById(userId);

        // Delete must not be called
        verify(repository, never()).deleteById(userId);
    }
}
