package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.UserRequestDTO;
import com.saipreety.taskmanagement.dto.UserResponseDTO;
import com.saipreety.taskmanagement.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.saipreety.taskmanagement.entity.UserEntity;
import com.saipreety.taskmanagement.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.saipreety.taskmanagement.entity.Role;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
		this.passwordEncoder = passwordEncoder;
    }

    private UserResponseDTO mapToResponse(UserEntity user){
		UserResponseDTO response = new UserResponseDTO();
		response.setId(user.getId());
		response.setFullName(user.getFullName());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole());
		response.setCreatedAt(user.getCreatedAt());
		return response;
	}

	public UserResponseDTO createUser(UserRequestDTO request){
		UserEntity user = new UserEntity();
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		user.setCreatedAt(LocalDateTime.now());
		UserEntity savedUser = repository.save(user);
		return mapToResponse(savedUser);
	}

	public List<UserResponseDTO> getAllUsers(){
		List<UserEntity> users = repository.findAll();
		List<UserResponseDTO> responseList = new ArrayList<>();
		for (UserEntity user : users) {
			responseList.add(mapToResponse(user));
		}
		return responseList;
	}

	public UserResponseDTO getUserById(Long id){
		Optional<UserEntity> optionalUser = repository.findById(id);
		if(optionalUser.isPresent()){
			UserEntity user = optionalUser.get();
			return mapToResponse(user);
		} else {
			throw new UserNotFoundException("User not found with id: " + id);
		}
	}

	public UserResponseDTO updateUser(@Valid Long id, UserRequestDTO request){
		Optional<UserEntity> userId = repository.findById(id);
		if(userId.isPresent()){
			UserEntity user = userId.get();
			user.setFullName(request.getFullName());
			user.setEmail(request.getEmail());
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			UserEntity savedUser = repository.save(user);

			return mapToResponse(savedUser);
		}
		else {
			throw new UserNotFoundException("User not found with id: " + id);
		}
	}

	public ResponseEntity<Object> deleteUser(Long id){
		Optional<UserEntity> userId = repository.findById(id);
		if(userId.isPresent()){
			repository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
		}
		else {
			throw new UserNotFoundException(
					"User not found with id: " + id
			);
		}
	}
}

//	public ResponseEntity<Object> updateUser(UserEntity user, Long id){
//		Optional<UserEntity> userId = repository.findById(id);
//		if(userId.isPresent()){
//			UserEntity existingUser = userId.get();
//			existingUser.setFullName(user.getFullName());
//			existingUser.setRole(Role.USER);
//			existingUser.setEmail(user.getEmail());
//			existingUser.setPassword(user.getPassword());
//			existingUser.setCreatedAt(LocalDateTime.now());
//			repository.save(existingUser);
//			return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
//		}
//		else {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist with this id");
//		}
//	}

//	public ResponseEntity<Object> getUsers(){
//		return ResponseEntity.status(HttpStatus.OK).body(repository.findAll());
//	}

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

//	public ResponseEntity<Object> createUser(UserEntity user) {
//		user.setRole(Role.USER);
//		user.setCreatedAt(LocalDateTime.now());
//		repository.save(user);
//		return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
//	}
//