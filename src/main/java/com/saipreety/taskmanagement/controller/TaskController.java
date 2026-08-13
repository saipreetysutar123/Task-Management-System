package com.saipreety.taskmanagement.controller;

import com.saipreety.taskmanagement.dto.TaskRequestDTO;
import com.saipreety.taskmanagement.dto.TaskResponseDTO;
import com.saipreety.taskmanagement.entity.TaskPriority;
import com.saipreety.taskmanagement.entity.TaskStatus;
import com.saipreety.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/task")
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
    public ResponseEntity<List<TaskResponseDTO>> getByStatus(@PathVariable TaskStatus status){
        return ResponseEntity.status(HttpStatus.OK).body(service.getTasksByStatus(status));
    }

    @GetMapping("/fetch/priority/{priority}")
    public ResponseEntity<List<TaskResponseDTO>> getByPriority(@PathVariable TaskPriority priority){
        return ResponseEntity.status(HttpStatus.OK).body(service.getTasksByPriority(priority));
    }

    @GetMapping("/fetch/project/{projectId}")
    public ResponseEntity<List<TaskResponseDTO>> getByProjectId(@PathVariable Long projectId){
        return ResponseEntity.status(HttpStatus.OK).body(service.getTasksByProjectId(projectId));
    }

    @GetMapping("/fetch/user/{userId}")
    public ResponseEntity<List<TaskResponseDTO>> getByUserId(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(service.getTasksByUser(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponseDTO>> search(
            @RequestParam String title
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.searchTasksByTitle(title));
    }
}
