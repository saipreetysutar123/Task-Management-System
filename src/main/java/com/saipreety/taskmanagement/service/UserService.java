package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

	ResponseEntity<Object> createUser(UserEntity user);
	ResponseEntity<Object> getUsers();
	ResponseEntity<Object> updateUser(UserEntity user, Long id);
	ResponseEntity<Object> deleteUser(Long id);
	ResponseEntity<Object> getById(Long id);
}
