package com.saipreety.taskmanagement.controller;

import com.saipreety.taskmanagement.dto.TaskRequestDTO;
import com.saipreety.taskmanagement.dto.TaskResponseDTO;
import com.saipreety.taskmanagement.entity.TaskPriority;
import com.saipreety.taskmanagement.entity.TaskStatus;
import com.saipreety.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/task")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service){
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTask(request));
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<Page<TaskResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllTasks(page, size, sortBy, direction));
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<TaskResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getTaskById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskResponseDTO> update(@Valid @RequestBody TaskRequestDTO request, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateTask(request, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        service.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
    }

    @GetMapping("/fetch/status/{status}")
    public ResponseEntity<Page<TaskResponseDTO>> getByStatus(
            @PathVariable TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction){
        return ResponseEntity.status(HttpStatus.OK).body(service.getTasksByStatus(
                status,
                page,
                size,
                sortBy,
                direction
        ));
    }

    @GetMapping("/fetch/priority/{priority}")
    public ResponseEntity<Page<TaskResponseDTO>> getByPriority(
            @PathVariable TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(
                service.getTasksByPriority(
                        priority,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/fetch/project/{projectId}")
    public ResponseEntity<Page<TaskResponseDTO>> getByProjectId(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(
                service.getTasksByProjectId(
                        projectId,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/fetch/user/{userId}")
    public ResponseEntity<Page<TaskResponseDTO>> getByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(
                service.getTasksByUser(
                        userId,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TaskResponseDTO>> searchTasks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(
                service.searchTasks(
                        keyword,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }
}