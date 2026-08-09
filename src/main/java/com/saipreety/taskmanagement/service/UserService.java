package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.UserRequestDTO;
import com.saipreety.taskmanagement.dto.UserResponseDTO;
import com.saipreety.taskmanagement.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

	UserResponseDTO createUser(UserRequestDTO request);
	List<UserResponseDTO> getAllUsers();
	UserResponseDTO updateUser(Long id, UserRequestDTO request);
	ResponseEntity<Object> deleteUser(Long id);
	UserResponseDTO getUserById(Long id);
}
