package com.jobportal.api.service;

import com.jobportal.api.dto.user.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getUserById(Long id);

    ResponseEntity<?> createUser(UserDTO userDTO);

    ResponseEntity<?> updateUser(UserDTO userDTO);

    ResponseEntity<?> removeUserById(Long id);

    Boolean CheckEmailExists(String email);
}
