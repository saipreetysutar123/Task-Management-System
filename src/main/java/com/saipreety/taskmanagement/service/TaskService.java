package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.TaskRequestDTO;
import com.saipreety.taskmanagement.dto.TaskResponseDTO;
import com.saipreety.taskmanagement.entity.TaskEntity;
import com.saipreety.taskmanagement.entity.TaskPriority;
import com.saipreety.taskmanagement.entity.TaskStatus;

import java.util.List;

public interface TaskService {

    TaskResponseDTO createTask(TaskRequestDTO request);
    List<TaskResponseDTO> getAllTasks();
    TaskResponseDTO getTaskById(Long id);
    TaskResponseDTO updateTask(TaskRequestDTO request, Long id);
    void deleteTask(Long id);
    List<TaskResponseDTO> getTasksByStatus(TaskStatus status);
    List<TaskResponseDTO> getTasksByPriority(TaskPriority priority);
    List<TaskResponseDTO> getTasksByProjectId(Long projectId);
    List<TaskResponseDTO> getTasksByUser(Long userId);
}
