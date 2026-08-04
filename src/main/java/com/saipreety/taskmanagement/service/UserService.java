package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.UserRequestDTO;
import com.saipreety.taskmanagement.dto.UserResponseDTO;
import com.saipreety.taskmanagement.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

	UserResponseDTO createUser(UserRequestDTO request);
	List<UserResponseDTO> getAllUsers();
	ResponseEntity<Object> updateUser(UserEntity user, Long id);
	ResponseEntity<Object> deleteUser(Long id);
	UserResponseDTO getUserById(Long id);
}
