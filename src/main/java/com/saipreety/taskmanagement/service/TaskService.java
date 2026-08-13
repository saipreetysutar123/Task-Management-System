package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.TaskRequestDTO;
import com.saipreety.taskmanagement.dto.TaskResponseDTO;
import com.saipreety.taskmanagement.entity.TaskEntity;
import com.saipreety.taskmanagement.entity.TaskPriority;
import com.saipreety.taskmanagement.entity.TaskStatus;
import org.springframework.data.domain.Page;

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
    List<TaskResponseDTO> getTasksByStatus(TaskStatus status);
    List<TaskResponseDTO> getTasksByPriority(TaskPriority priority);
    List<TaskResponseDTO> getTasksByProjectId(Long projectId);
    List<TaskResponseDTO> getTasksByUser(Long userId);
    List<TaskResponseDTO> searchTasksByTitle(String title);
}
