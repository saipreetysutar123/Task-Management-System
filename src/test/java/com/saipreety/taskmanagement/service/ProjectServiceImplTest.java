package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.ProjectRequestDTO;
import com.saipreety.taskmanagement.dto.ProjectResponseDTO;
import com.saipreety.taskmanagement.entity.ProjectEntity;
import com.saipreety.taskmanagement.exception.ProjectNotFoundException;
import com.saipreety.taskmanagement.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDateTime;
import static org.mockito.Mockito.verify;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

    @Mock
    private ProjectRepository repository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void createProject_ShouldCreateAndReturnProject() {

        // Arrange
        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("Task Management System");
        request.setDescription("Project for managing tasks and users");

        ProjectEntity savedProject = new ProjectEntity();
        savedProject.setId(1L);
        savedProject.setName("Task Management System");
        savedProject.setDescription("Project for managing tasks and users");
        savedProject.setCreatedAt(LocalDateTime.now());

        when(repository.save(any(ProjectEntity.class)))
                .thenReturn(savedProject);

        // Act
        ProjectResponseDTO result =
                projectService.createProject(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(
                "Task Management System",
                result.getName()
        );
        assertEquals(
                "Project for managing tasks and users",
                result.getDescription()
        );
        assertNotNull(result.getCreatedAt());

        verify(repository).save(any(ProjectEntity.class));
    }

    @Test
    void getAllProjects_ShouldReturnAllProjects() {

        // Arrange
        ProjectEntity project1 = new ProjectEntity();
        project1.setId(1L);
        project1.setName("Task Management System");
        project1.setDescription("Project for managing tasks");
        project1.setCreatedAt(LocalDateTime.now());

        ProjectEntity project2 = new ProjectEntity();
        project2.setId(2L);
        project2.setName("Resume Builder");
        project2.setDescription("Project for building resumes");
        project2.setCreatedAt(LocalDateTime.now());

        when(repository.findAll())
                .thenReturn(List.of(project1, project2));

        // Act
        List<ProjectResponseDTO> result =
                projectService.getAllProjects();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(
                "Task Management System",
                result.get(0).getName()
        );
        assertEquals(
                "Resume Builder",
                result.get(1).getName()
        );
        verify(repository).findAll();
    }

    @Test
    void getProjectById_WhenProjectExists_ShouldReturnProject() {

        // Arrange
        Long projectId = 1L;

        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);
        project.setName("Task Management System");
        project.setDescription("Project for managing tasks");
        project.setCreatedAt(LocalDateTime.now());

        when(repository.findById(projectId))
                .thenReturn(Optional.of(project));

        // Act
        ProjectResponseDTO result =
                projectService.getProjectById(projectId);

        // Assert
        assertNotNull(result);
        assertEquals(projectId, result.getId());
        assertEquals(
                "Task Management System",
                result.getName()
        );
        assertEquals(
                "Project for managing tasks",
                result.getDescription()
        );

        verify(repository).findById(projectId);
    }

    @Test
    void getProjectById_WhenProjectDoesNotExist_ShouldThrowException() {

        // Arrange
        Long projectId = 99L;

        when(repository.findById(projectId))
                .thenReturn(Optional.empty());

        // Act & Assert
        ProjectNotFoundException exception = assertThrows(
                ProjectNotFoundException.class,
                () -> projectService.getProjectById(projectId)
        );

        assertEquals(
                "Project not found with id: 99",
                exception.getMessage()
        );

        verify(repository).findById(projectId);
    }

    @Test
    void updateProject_WhenProjectExists_ShouldUpdateProject() {

        // Arrange
        Long projectId = 1L;

        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("Updated Task Management System");
        request.setDescription("Updated project description");

        ProjectEntity existingProject = new ProjectEntity();
        existingProject.setId(projectId);
        existingProject.setName("Old Project Name");
        existingProject.setDescription("Old project description");
        existingProject.setCreatedAt(LocalDateTime.now());

        when(repository.findById(projectId))
                .thenReturn(Optional.of(existingProject));

        when(repository.save(existingProject))
                .thenReturn(existingProject);

        // Act
        ProjectResponseDTO result =
                projectService.updateProject(request, projectId);

        // Assert
        assertNotNull(result);
        assertEquals(
                projectId,
                result.getId()
        );
        assertEquals(
                "Updated Task Management System",
                result.getName()
        );
        assertEquals(
                "Updated project description",
                result.getDescription()
        );

        verify(repository).findById(projectId);
        verify(repository).save(existingProject);
    }

    @Test
    void updateProject_WhenProjectDoesNotExist_ShouldThrowException() {

        // Arrange
        Long projectId = 99L;

        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setName("Updated Project");
        request.setDescription("Updated project description");

        when(repository.findById(projectId))
                .thenReturn(Optional.empty());

        // Act & Assert
        ProjectNotFoundException exception = assertThrows(
                ProjectNotFoundException.class,
                () -> projectService.updateProject(request, projectId)
        );

        assertEquals(
                "Project not found with id: 99",
                exception.getMessage()
        );

        verify(repository).findById(projectId);
        verify(repository, never()).save(any(ProjectEntity.class));
    }

    @Test
    void deleteProject_WhenProjectExists_ShouldDeleteProject() {

        // Arrange
        Long projectId = 1L;

        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);

        when(repository.findById(projectId))
                .thenReturn(Optional.of(project));

        // Act
        projectService.deleteProject(projectId);

        // Assert
        verify(repository).findById(projectId);
        verify(repository).deleteById(projectId);
    }

    @Test
    void deleteProject_WhenProjectDoesNotExist_ShouldThrowException() {

        // Arrange
        Long projectId = 99L;

        when(repository.findById(projectId))
                .thenReturn(Optional.empty());

        // Act & Assert
        ProjectNotFoundException exception = assertThrows(
                ProjectNotFoundException.class,
                () -> projectService.deleteProject(projectId)
        );

        assertEquals(
                "Project not found with id: 99",
                exception.getMessage()
        );

        verify(repository).findById(projectId);

        // Delete must not be called
        verify(repository, never()).deleteById(projectId);
    }
}
