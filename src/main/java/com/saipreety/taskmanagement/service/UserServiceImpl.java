package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.UserRequestDTO;
import com.saipreety.taskmanagement.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.saipreety.taskmanagement.entity.UserEntity;
import com.saipreety.taskmanagement.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.saipreety.taskmanagement.entity.Role;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	public UserResponseDTO createUser(UserRequestDTO request){
		UserEntity user = new UserEntity();
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setRole(Role.USER);
		user.setCreatedAt(LocalDateTime.now());
		UserEntity savedUser = repository.save(user);

		UserResponseDTO response = new UserResponseDTO();
		response.setId(savedUser.getId());
		response.setFullName(savedUser.getFullName());
		response.setEmail(savedUser.getEmail());
		response.setRole(savedUser.getRole());
		response.setCreatedAt(savedUser.getCreatedAt());
		return response;
	}

//	public ResponseEntity<Object> createUser(UserEntity user) {
//		user.setRole(Role.USER);
//		user.setCreatedAt(LocalDateTime.now());
//		repository.save(user);
//		return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
//	}
//
	public List<UserResponseDTO> getAllUsers(){
		List<UserEntity> users = repository.findAll();
		List<UserResponseDTO> responseList = new ArrayList<>();
		for (UserEntity user : users) {
			UserResponseDTO response = new UserResponseDTO();
			response.setId(user.getId());
			response.setFullName(user.getFullName());
			response.setEmail(user.getEmail());
			response.setRole(user.getRole());
			response.setCreatedAt(user.getCreatedAt());
			responseList.add(response);
		}
		return responseList;
	}

//	public ResponseEntity<Object> getUsers(){
//		return ResponseEntity.status(HttpStatus.OK).body(repository.findAll());
//	}

	public UserResponseDTO getUserById(Long id){
		Optional<UserEntity> optionalUser = repository.findById(id);
		if(optionalUser.isPresent()){
			UserEntity user = optionalUser.get();
			UserResponseDTO response = new UserResponseDTO();
			response.setId(user.getId());
			response.setFullName(user.getFullName());
			response.setEmail(user.getEmail());
			response.setRole(user.getRole());
			response.setCreatedAt(user.getCreatedAt());
			return response;
		} else {
			return null;
		}
	}

//	public ResponseEntity<Object> getById(Long id){
//		Optional<UserEntity> userId = repository.findById(id);
//		if(userId.isPresent()){
//			repository.findById(id);
//			return ResponseEntity.status(HttpStatus.OK).body(repository.findById(id));
//		}
//		else {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//		}
//	}

	public ResponseEntity<Object> updateUser(UserEntity user, Long id){
		Optional<UserEntity> userId = repository.findById(id);
		if(userId.isPresent()){
			UserEntity existingUser = userId.get();
			existingUser.setFullName(user.getFullName());
			existingUser.setRole(Role.USER);
			existingUser.setEmail(user.getEmail());
			existingUser.setPassword(user.getPassword());
			existingUser.setCreatedAt(LocalDateTime.now());
			repository.save(existingUser);
			return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist with this id");
		}
	}

	public ResponseEntity<Object> deleteUser(Long id){
		Optional<UserEntity> userId = repository.findById(id);
		if(userId.isPresent()){
			repository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User "+ userId + " not found");
		}
	}
}