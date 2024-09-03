package com.jobportal.api.service;

import com.jobportal.api.dto.request.CreateUserRequest;
import com.jobportal.api.dto.request.UpdateUserRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getUserById(String id);

    ResponseEntity<?> createUser(CreateUserRequest createUserRequest);

    ResponseEntity<?> updateUser(UpdateUserRequest updateUserRequest);

    ResponseEntity<?> removeUserById(String id);

    ResponseEntity<?> getCurrentUser();

    Boolean CheckEmailExists(String email);
}
