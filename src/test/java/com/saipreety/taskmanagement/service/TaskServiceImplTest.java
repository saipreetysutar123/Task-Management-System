package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.TaskResponseDTO;
import com.saipreety.taskmanagement.entity.ProjectEntity;
import com.saipreety.taskmanagement.entity.TaskEntity;
import com.saipreety.taskmanagement.entity.TaskPriority;
import com.saipreety.taskmanagement.entity.TaskStatus;
import com.saipreety.taskmanagement.exception.ProjectNotFoundException;
import com.saipreety.taskmanagement.exception.TaskNotFoundException;
import com.saipreety.taskmanagement.repository.ProjectRepository;
import com.saipreety.taskmanagement.repository.TaskRepository;
import com.saipreety.taskmanagement.repository.UserRepository;
import com.saipreety.taskmanagement.entity.UserEntity;
import com.saipreety.taskmanagement.dto.TaskRequestDTO;
import com.saipreety.taskmanagement.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskServiceImpl taskService;


    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() {

        // Arrange
        Long taskId = 1L;

        UserEntity user = new UserEntity();
        user.setId(2L);

        ProjectEntity project = new ProjectEntity();
        project.setId(2L);

        TaskEntity task = new TaskEntity();
        task.setId(taskId);
        task.setTitle("Create Login API");
        task.setDescription("Implement login functionality");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setUser(user);
        task.setProject(project);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        // Act
        TaskResponseDTO result = taskService.getTaskById(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Create Login API", result.getTitle());
        assertEquals(TaskStatus.TODO, result.getStatus());
        assertEquals(TaskPriority.HIGH, result.getPriority());
        assertEquals(2L, result.getUserId());
        assertEquals(2L, result.getProjectId());

        verify(taskRepository).findById(taskId);
    }

    @Test
    void getTaskById_WhenTaskDoesNotExist_ShouldThrowException() {

        // Arrange
        Long taskId = 999L;

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        // Act & Assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(taskId)
        );

        assertEquals(
                "Task not found with id: 999",
                exception.getMessage()
        );

        verify(taskRepository).findById(taskId);
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDeleteTask() {

        // Arrange
        Long taskId = 1L;

        TaskEntity task = new TaskEntity();
        task.setId(taskId);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository).findById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldThrowException() {

        // Arrange
        Long taskId = 999L;

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        // Act & Assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask(taskId)
        );

        assertEquals(
                "Task not found with id: 999",
                exception.getMessage()
        );

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).deleteById(taskId);
    }

    @Test
    void createTask_WhenUserAndProjectExist_ShouldCreateTask() {

        // Arrange
        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle("Create Login API");
        request.setDescription("Implement login functionality");
        request.setStatus(TaskStatus.TODO);
        request.setPriority(TaskPriority.HIGH);
        request.setDueDate(LocalDate.of(2026, 8, 20));
        request.setUserId(2L);
        request.setProjectId(2L);

        UserEntity user = new UserEntity();
        user.setId(2L);

        ProjectEntity project = new ProjectEntity();
        project.setId(2L);

        TaskEntity savedTask = new TaskEntity();
        savedTask.setId(1L);
        savedTask.setTitle(request.getTitle());
        savedTask.setDescription(request.getDescription());
        savedTask.setStatus(request.getStatus());
        savedTask.setPriority(request.getPriority());
        savedTask.setDueDate(request.getDueDate());
        savedTask.setUser(user);
        savedTask.setProject(project);
        savedTask.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(projectRepository.findById(2L))
                .thenReturn(Optional.of(project));

        when(taskRepository.save(any(TaskEntity.class)))
                .thenReturn(savedTask);

        // Act
        TaskResponseDTO result = taskService.createTask(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Create Login API", result.getTitle());
        assertEquals(TaskStatus.TODO, result.getStatus());
        assertEquals(TaskPriority.HIGH, result.getPriority());
        assertEquals(2L, result.getUserId());
        assertEquals(2L, result.getProjectId());

        verify(userRepository).findById(2L);
        verify(projectRepository).findById(2L);
        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    void createTask_WhenUserDoesNotExist_ShouldThrowException() {

        // Arrange
        TaskRequestDTO request = new TaskRequestDTO();
        request.setUserId(999L);
        request.setProjectId(2L);

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> taskService.createTask(request)
        );

        assertEquals(
                "User not found with this id: 999",
                exception.getMessage()
        );

        verify(userRepository).findById(999L);

        // Project and task repositories should never be called
        verifyNoInteractions(projectRepository);
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void createTask_WhenProjectDoesNotExist_ShouldThrowException() {

        // Arrange
        TaskRequestDTO request = new TaskRequestDTO();
        request.setUserId(2L);
        request.setProjectId(999L);

        UserEntity user = new UserEntity();
        user.setId(2L);

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(projectRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        ProjectNotFoundException exception = assertThrows(
                ProjectNotFoundException.class,
                () -> taskService.createTask(request)
        );

        assertEquals(
                "Project not found with this id: 999",
                exception.getMessage()
        );

        verify(userRepository).findById(2L);
        verify(projectRepository).findById(999L);

        // Task should never be saved
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void updateTask_WhenTaskUserAndProjectExist_ShouldUpdateTask() {

        // Arrange
        Long taskId = 1L;

        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle("Updated Login API");
        request.setDescription("Updated login functionality using Spring Security");
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setPriority(TaskPriority.MEDIUM);
        request.setDueDate(LocalDate.of(2026, 8, 25));
        request.setUserId(2L);
        request.setProjectId(2L);

        UserEntity user = new UserEntity();
        user.setId(2L);

        ProjectEntity project = new ProjectEntity();
        project.setId(2L);

        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(taskId);
        existingTask.setUser(user);
        existingTask.setProject(project);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(existingTask));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(projectRepository.findById(2L))
                .thenReturn(Optional.of(project));

        when(taskRepository.save(any(TaskEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskResponseDTO result =
                taskService.updateTask(request, taskId);

        // Assert
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Updated Login API", result.getTitle());
        assertEquals(
                "Updated login functionality using Spring Security",
                result.getDescription()
        );
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
        assertEquals(TaskPriority.MEDIUM, result.getPriority());
        assertEquals(
                LocalDate.of(2026, 8, 25),
                result.getDueDate()
        );
        assertEquals(2L, result.getUserId());
        assertEquals(2L, result.getProjectId());

        verify(taskRepository).findById(taskId);
        verify(userRepository).findById(2L);
        verify(projectRepository).findById(2L);
        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    void updateTask_WhenTaskDoesNotExist_ShouldThrowException() {

        // Arrange
        Long taskId = 999L;

        TaskRequestDTO request = new TaskRequestDTO();
        request.setUserId(2L);
        request.setProjectId(2L);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        // Act & Assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.updateTask(request, taskId)
        );

        assertEquals(
                "Task not found with id: 999",
                exception.getMessage()
        );

        verify(taskRepository).findById(taskId);

        // These should not be reached
        verifyNoInteractions(userRepository);
        verifyNoInteractions(projectRepository);
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void updateTask_WhenUserDoesNotExist_ShouldThrowException() {

        // Arrange
        Long taskId = 1L;

        TaskRequestDTO request = new TaskRequestDTO();
        request.setUserId(999L);
        request.setProjectId(2L);

        TaskEntity task = new TaskEntity();
        task.setId(taskId);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> taskService.updateTask(request, taskId)
        );

        assertEquals(
                "User not found with this id: 999",
                exception.getMessage()
        );

        verify(taskRepository).findById(taskId);
        verify(userRepository).findById(999L);

        verifyNoInteractions(projectRepository);
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void updateTask_WhenProjectDoesNotExist_ShouldThrowException() {

        // Arrange
        Long taskId = 1L;

        TaskRequestDTO request = new TaskRequestDTO();
        request.setUserId(2L);
        request.setProjectId(999L);

        TaskEntity task = new TaskEntity();
        task.setId(taskId);

        UserEntity user = new UserEntity();
        user.setId(2L);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(projectRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        ProjectNotFoundException exception = assertThrows(
                ProjectNotFoundException.class,
                () -> taskService.updateTask(request, taskId)
        );

        assertEquals(
                "Project not found with this id: 999",
                exception.getMessage()
        );

        verify(taskRepository).findById(taskId);
        verify(userRepository).findById(2L);
        verify(projectRepository).findById(999L);

        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void getTasksByStatus_WhenSortFieldIsInvalid_ShouldThrowException() {

        // Arrange
        TaskStatus status = TaskStatus.TODO;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getTasksByStatus(
                        status,
                        0,
                        5,
                        "hello",
                        "asc"
                )
        );

        assertEquals(
                "Invalid sort field: hello",
                exception.getMessage()
        );

        verifyNoInteractions(taskRepository);
    }

    @Test
    void getTasksByStatus_WhenSortDirectionIsInvalid_ShouldThrowException() {

        // Arrange
        TaskStatus status = TaskStatus.TODO;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getTasksByStatus(
                        status,
                        0,
                        5,
                        "id",
                        "hello"
                )
        );

        assertEquals(
                "Invalid sort direction: hello",
                exception.getMessage()
        );

        verifyNoInteractions(taskRepository);
    }

    @Test
    void getTasksByStatus_WhenPageIsNegative_ShouldThrowException() {

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getTasksByStatus(
                        TaskStatus.TODO,
                        -1,
                        5,
                        "id",
                        "asc"
                )
        );

        assertEquals(
                "Page number cannot be negative",
                exception.getMessage()
        );

        verifyNoInteractions(taskRepository);
    }

    @Test
    void getTasksByStatus_WhenPageSizeIsInvalid_ShouldThrowException() {

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getTasksByStatus(
                        TaskStatus.TODO,
                        0,
                        0,
                        "id",
                        "asc"
                )
        );

        assertEquals(
                "Page size must be greater than 0",
                exception.getMessage()
        );

        verifyNoInteractions(taskRepository);
    }

    @Test
    void getTasksByStatus_WhenValidPaginationAndSorting_ShouldReturnTasks() {

        // Arrange
        TaskStatus status = TaskStatus.TODO;

        TaskEntity task = new TaskEntity();
        task.setId(1L);
        task.setTitle("Create Login API");
        task.setDescription("Implement login functionality");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);

        UserEntity user = new UserEntity();
        user.setId(2L);

        ProjectEntity project = new ProjectEntity();
        project.setId(2L);

        task.setUser(user);
        task.setProject(project);

        Page<TaskEntity> taskPage =
                new PageImpl<>(List.of(task));

        when(taskRepository.findByStatus(
                eq(status),
                any(Pageable.class)
        )).thenReturn(taskPage);

        // Act
        Page<TaskResponseDTO> result =
                taskService.getTasksByStatus(
                        status,
                        0,
                        5,
                        "id",
                        "asc"
                );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(
                "Create Login API",
                result.getContent().get(0).getTitle()
        );

        verify(taskRepository).findByStatus(
                eq(status),
                any(Pageable.class)
        );
    }

    @Test
    void getTasksByPriority_WhenValidPagination_ShouldReturnTasks() {

        // Arrange
        TaskPriority priority = TaskPriority.HIGH;

        UserEntity user = new UserEntity();
        user.setId(2L);

        ProjectEntity project = new ProjectEntity();
        project.setId(2L);

        TaskEntity task = new TaskEntity();
        task.setId(1L);
        task.setTitle("Create Login API");
        task.setDescription("Implement login functionality");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setUser(user);
        task.setProject(project);

        Page<TaskEntity> taskPage =
                new PageImpl<>(List.of(task));

        when(taskRepository.findByPriority(
                eq(priority),
                any(Pageable.class)
        )).thenReturn(taskPage);

        // Act
        Page<TaskResponseDTO> result =
                taskService.getTasksByPriority(
                        priority,
                        0,
                        5,
                        "id",
                        "asc"
                );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(
                "Create Login API",
                result.getContent().get(0).getTitle()
        );
        assertEquals(
                TaskPriority.HIGH,
                result.getContent().get(0).getPriority()
        );

        verify(taskRepository).findByPriority(
                eq(priority),
                any(Pageable.class)
        );
    }

    @Test
    void getTasksByProjectId_WhenValidPagination_ShouldReturnTasks() {

        // Arrange
        Long projectId = 2L;

        UserEntity user = new UserEntity();
        user.setId(2L);

        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);

        TaskEntity task = new TaskEntity();
        task.setId(1L);
        task.setTitle("Create Login API");
        task.setDescription("Implement login functionality");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setUser(user);
        task.setProject(project);

        Page<TaskEntity> taskPage =
                new PageImpl<>(List.of(task));

        when(taskRepository.findByProjectId(
                eq(projectId),
                any(Pageable.class)
        )).thenReturn(taskPage);

        // Act
        Page<TaskResponseDTO> result =
                taskService.getTasksByProjectId(
                        projectId,
                        0,
                        5,
                        "id",
                        "asc"
                );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        assertEquals(
                "Create Login API",
                result.getContent().get(0).getTitle()
        );

        assertEquals(
                2L,
                result.getContent().get(0).getProjectId()
        );

        verify(taskRepository).findByProjectId(
                eq(projectId),
                any(Pageable.class)
        );
    }

    @Test
    void getTasksByUser_WhenValidPagination_ShouldReturnTasks() {

        // Arrange
        Long userId = 2L;

        UserEntity user = new UserEntity();
        user.setId(userId);

        ProjectEntity project = new ProjectEntity();
        project.setId(2L);

        TaskEntity task = new TaskEntity();
        task.setId(1L);
        task.setTitle("Create Login API");
        task.setDescription("Implement login functionality");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setUser(user);
        task.setProject(project);

        Page<TaskEntity> taskPage =
                new PageImpl<>(List.of(task));

        when(taskRepository.findByUserId(
                eq(userId),
                any(Pageable.class)
        )).thenReturn(taskPage);

        // Act
        Page<TaskResponseDTO> result =
                taskService.getTasksByUser(
                        userId,
                        0,
                        5,
                        "id",
                        "asc"
                );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        assertEquals(
                "Create Login API",
                result.getContent().get(0).getTitle()
        );

        assertEquals(
                2L,
                result.getContent().get(0).getUserId()
        );

        verify(taskRepository).findByUserId(
                eq(userId),
                any(Pageable.class)
        );
    }

    @Test
    void searchTasksByTitle_WhenValidKeyword_ShouldReturnTasks() {

        // Arrange
        String keyword = "Login";

        UserEntity user = new UserEntity();
        user.setId(2L);

        ProjectEntity project = new ProjectEntity();
        project.setId(2L);

        TaskEntity task = new TaskEntity();
        task.setId(1L);
        task.setTitle("Create Login API");
        task.setDescription("Implement login functionality");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setUser(user);
        task.setProject(project);

        Page<TaskEntity> taskPage =
                new PageImpl<>(List.of(task));

        when(taskRepository.findByTitleContainingIgnoreCase(
                eq(keyword),
                any(Pageable.class)
        )).thenReturn(taskPage);

        // Act
        Page<TaskResponseDTO> result =
                taskService.searchTasks(
                        keyword,
                        0,
                        5,
                        "id",
                        "asc"
                );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        assertEquals(
                "Create Login API",
                result.getContent().get(0).getTitle()
        );

        verify(taskRepository).findByTitleContainingIgnoreCase(
                eq(keyword),
                any(Pageable.class)
        );
    }

    @Test
    void searchTasks_WhenValidKeywordAndPagination_ShouldReturnTasks() {

        // Arrange
        String keyword = "Login";

        UserEntity user = new UserEntity();
        user.setId(2L);

        ProjectEntity project = new ProjectEntity();
        project.setId(2L);

        TaskEntity task = new TaskEntity();
        task.setId(1L);
        task.setTitle("Create Login API");
        task.setDescription("Implement login functionality");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setUser(user);
        task.setProject(project);

        Page<TaskEntity> taskPage =
                new PageImpl<>(List.of(task));

        when(taskRepository.findByTitleContainingIgnoreCase(
                eq(keyword),
                any(Pageable.class)
        )).thenReturn(taskPage);

        // Act
        Page<TaskResponseDTO> result =
                taskService.searchTasks(
                        keyword,
                        0,
                        5,
                        "id",
                        "asc"
                );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        assertEquals(
                "Create Login API",
                result.getContent().get(0).getTitle()
        );

        assertEquals(
                TaskStatus.TODO,
                result.getContent().get(0).getStatus()
        );

        assertEquals(
                TaskPriority.HIGH,
                result.getContent().get(0).getPriority()
        );

        verify(taskRepository).findByTitleContainingIgnoreCase(
                eq(keyword),
                any(Pageable.class)
        );
    }
}