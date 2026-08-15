package com.saipreety.taskmanagement.controller;

import com.saipreety.taskmanagement.dto.ProjectRequestDTO;
import com.saipreety.taskmanagement.dto.ProjectResponseDTO;
import com.saipreety.taskmanagement.service.ProjectService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/project")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service){
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDTO> create(@Valid @RequestBody ProjectRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProject(request));
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<ProjectResponseDTO>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllProjects());
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<ProjectResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getProjectById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProjectResponseDTO> update(@Valid @RequestBody ProjectRequestDTO request, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateProject(request, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        service.deleteProject(id);
        return ResponseEntity.status(HttpStatus.OK).body("Project deleted successfully");
    }
}
