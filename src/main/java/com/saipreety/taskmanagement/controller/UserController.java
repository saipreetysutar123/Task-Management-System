package com.saipreety.taskmanagement.controller;

import com.saipreety.taskmanagement.dto.UserRequestDTO;
import com.saipreety.taskmanagement.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.saipreety.taskmanagement.entity.UserEntity;
import com.saipreety.taskmanagement.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
	
	@Autowired
	private UserService service;

	@PostMapping("/create")
	public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO request){
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(request));
	}

	@GetMapping("/fetchAll")
	public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
		return ResponseEntity.status(HttpStatus.OK).body(service.getAllUsers());
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id){
		UserResponseDTO response = service.getUserById(id);
		if(response != null){
			return ResponseEntity.ok(response);
		}
		else{
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		 }
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<UserResponseDTO> update(@Valid @RequestBody UserRequestDTO request, @PathVariable Long id){
		UserResponseDTO response = service.updateUser(id, request);
		if(response != null){
			return ResponseEntity.ok(response);
		}
		else{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> delete(UserEntity user, @PathVariable Long id){
		return service.deleteUser(id);
	}
}
//	@PostMapping("/create")
//	public ResponseEntity<Object> create(@Valid @RequestBody UserEntity user) {
//	    System.out.println(user.getFullName());
//	    System.out.println(user.getEmail());
//	    System.out.println(user.getPassword());
//		return service.createUser(user);
//	}

//	@GetMapping("/fetch")
//	public ResponseEntity<Object> getAllUser(){
//		return service.getUsers();
//	}