package com.saipreety.taskmanagement.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.saipreety.taskmanagement.entity.UserEntity;
import com.saipreety.taskmanagement.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;

	@PostMapping("/create")
	public ResponseEntity<Object> create(@Valid @RequestBody UserEntity user) {
	    System.out.println(user.getFullName());
	    System.out.println(user.getEmail());
	    System.out.println(user.getPassword());
		return service.createUser(user);
	}

	@GetMapping("/fetch")
	public ResponseEntity<Object> getAllUser(){
		return service.getUsers();
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<Object> getById(@PathVariable Long id){
		return service.getById(id);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> update(@RequestBody UserEntity user, @PathVariable Long id){
		return service.updateUser(user, id);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> delete(UserEntity user, @PathVariable Long id){
		return service.deleteUser(id);
	}
}
