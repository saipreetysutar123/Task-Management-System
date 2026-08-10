package com.saipreety.taskmanagement.controller;

import com.saipreety.taskmanagement.dto.TaskRequestDTO;
import com.saipreety.taskmanagement.dto.TaskResponseDTO;
import com.saipreety.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<TaskResponseDTO>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllTasks());
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
}
