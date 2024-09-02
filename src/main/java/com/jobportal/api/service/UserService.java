package com.jobportal.api.service;

import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.dto.request.LoginRequest;
import com.jobportal.api.dto.request.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getUserById(Long id);

    ResponseEntity<?> createUser(UserDTO userDTO);

    ResponseEntity<?> updateUser(UserDTO userDTO);

    ResponseEntity<?> removeUserById(Long id);

    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> register(RegisterRequest registerRequest);

    ResponseEntity<?> resetPassword(String email, String newPassword);

    Boolean CheckEmailExists(String email);
}
