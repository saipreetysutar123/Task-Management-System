package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.TaskRequestDTO;
import com.saipreety.taskmanagement.dto.TaskResponseDTO;
import java.util.List;

public interface TaskService {

    TaskResponseDTO createTask(TaskRequestDTO request);
    List<TaskResponseDTO> getAllTasks();
    TaskResponseDTO getTaskById(Long id);
    TaskResponseDTO updateTask(TaskRequestDTO request, Long id);
    void deleteTask(Long id);
}
