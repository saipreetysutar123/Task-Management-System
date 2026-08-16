package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.TaskRequestDTO;
import com.saipreety.taskmanagement.dto.TaskResponseDTO;
import com.saipreety.taskmanagement.entity.TaskEntity;
import com.saipreety.taskmanagement.entity.TaskPriority;
import com.saipreety.taskmanagement.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    TaskResponseDTO createTask(TaskRequestDTO request);
    Page<TaskResponseDTO> getAllTasks(
            int page,
            int size,
            String sortBy,
            String direction
    );
    TaskResponseDTO getTaskById(Long id);
    TaskResponseDTO updateTask(TaskRequestDTO request, Long id);
    void deleteTask(Long id);
    Page<TaskResponseDTO> getTasksByStatus(
            TaskStatus status,
            int page,
            int size,
            String sortBy,
            String direction
    );
    Page<TaskResponseDTO> getTasksByPriority(
            TaskPriority priority,
            int page,
            int size,
            String sortBy,
            String direction
    );
    Page<TaskResponseDTO> getTasksByProjectId(
            Long projectId,
            int page,
            int size,
            String sortBy,
            String direction
    );
    Page<TaskResponseDTO> getTasksByUser(
            Long userId,
            int page,
            int size,
            String sortBy,
            String direction
    );
    Page<TaskResponseDTO> searchTasks(
            String keyword,
            int page,
            int size,
            String sortBy,
            String direction
    );
}
