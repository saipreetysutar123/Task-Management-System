package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.TaskRequestDTO;
import com.saipreety.taskmanagement.dto.TaskResponseDTO;
import com.saipreety.taskmanagement.entity.*;
import com.saipreety.taskmanagement.exception.ProjectNotFoundException;
import com.saipreety.taskmanagement.exception.TaskNotFoundException;
import com.saipreety.taskmanagement.exception.UserNotFoundException;
import com.saipreety.taskmanagement.repository.ProjectRepository;
import com.saipreety.taskmanagement.repository.TaskRepository;
import com.saipreety.taskmanagement.repository.UserRepository;
import com.saipreety.taskmanagement.util.PaginationValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            UserRepository userRepository,
            ProjectRepository projectRepository
    ){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    private boolean isValidSortField(String sortBy) {

        return switch (sortBy) {
            case "id",
                 "title",
                 "description",
                 "status",
                 "priority",
                 "createdAt",
                 "dueDate" -> true;

            default -> false;
        };
    }

    private TaskResponseDTO mapToResponse(TaskEntity task){
        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUserId(task.getUser().getId());
        response.setProjectId(task.getProject().getId());
        return response;
    }

    public TaskResponseDTO createTask(TaskRequestDTO request){
        TaskEntity task = new TaskEntity();
        UserEntity user = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found with this id: " + request.getUserId()));
        task.setUser(user);
        ProjectEntity project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new ProjectNotFoundException("Project not found with this id: "+ request.getProjectId()));
        task.setProject(project);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setCreatedAt(LocalDateTime.now());
        TaskEntity savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    public Page<TaskResponseDTO> getAllTasks(
            int page,
            int size,
            String sortBy,
            String direction
    ){
        PaginationValidator.validate(page, size);
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TaskEntity> tasks = taskRepository.findAll(pageable);

        return tasks.map(this::mapToResponse);
    }

    public TaskResponseDTO getTaskById(Long id){
        Optional<TaskEntity> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            TaskEntity task = optionalTask.get();
            return mapToResponse(task);
        } else {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
    }

    public TaskResponseDTO updateTask(TaskRequestDTO request, Long id){
        Optional<TaskEntity> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            TaskEntity task = optionalTask.get();
            UserEntity user = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found with this id: " + request.getUserId()));
            task.setUser(user);
            ProjectEntity project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new ProjectNotFoundException("Project not found with this id: "+ request.getProjectId()));
            task.setProject(project);
            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            task.setStatus(request.getStatus());
            task.setPriority(request.getPriority());
            task.setDueDate(request.getDueDate());

            TaskEntity savedTask = taskRepository.save(task);

            return mapToResponse(savedTask);
        }
        else {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
    }

    public void deleteTask(Long id){
        Optional<TaskEntity> taskId = taskRepository.findById(id);
        if(taskId.isPresent()){
            taskRepository.deleteById(id);
        }
        else {
            throw new TaskNotFoundException(
                    "Task not found with id: " + id
            );
        }
    }

    public Page<TaskResponseDTO> getTasksByStatus(
            TaskStatus status,
            int page,
            int size,
            String sortBy,
            String direction
    ){
        PaginationValidator.validate(page, size);

        if (!isValidSortField(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy
            );
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Invalid sort direction: " + direction
            );
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TaskEntity> tasks = taskRepository.findByStatus(status, pageable);

        return tasks.map(this::mapToResponse);
    }

    public Page<TaskResponseDTO> getTasksByPriority(
            TaskPriority priority,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        PaginationValidator.validate(page, size);
        if (!isValidSortField(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy
            );
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Invalid sort direction: " + direction
            );
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TaskEntity> tasks =
                taskRepository.findByPriority(priority, pageable);

        return tasks.map(this::mapToResponse);
    }

    public Page<TaskResponseDTO> getTasksByProjectId(
            Long projectId,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        PaginationValidator.validate(page, size);
        if (!isValidSortField(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy
            );
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Invalid sort direction: " + direction
            );
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TaskEntity> tasks =
                taskRepository.findByProjectId(projectId, pageable);

        return tasks.map(this::mapToResponse);
    }

    public Page<TaskResponseDTO> getTasksByUser(
            Long userId,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        PaginationValidator.validate(page, size);
        if (!isValidSortField(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy
            );
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Invalid sort direction: " + direction
            );
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TaskEntity> tasks =
                taskRepository.findByUserId(userId, pageable);

        return tasks.map(this::mapToResponse);
    }

    public Page<TaskResponseDTO> searchTasks(
            String keyword,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        PaginationValidator.validate(page, size);
        if (!isValidSortField(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy
            );
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Invalid sort direction: " + direction
            );
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TaskEntity> tasks =
                taskRepository.findByTitleContainingIgnoreCase(
                        keyword,
                        pageable
                );

        return tasks.map(this::mapToResponse);
    }
}
