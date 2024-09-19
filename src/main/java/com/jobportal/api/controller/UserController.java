package com.jobportal.api.controller;

import com.jobportal.api.dto.request.user.CreateUserRequest;
import com.jobportal.api.dto.request.user.UpdateUserRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();

        String message = users.isEmpty() ? "No users found" : "Successfully retrieved all users";

        SuccessResponse<List<UserDTO>> successResponse = SuccessResponse.<List<UserDTO>>builder()
                .message(message)
                .result(users)
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserDTO>> getUserById(@PathVariable("id") String id) {
        UserDTO userDTO = userService.getUserById(id);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("User found")
                .result(userDTO)
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<UserDTO>> createUser(@RequestBody CreateUserRequest createUserRequest) {
        UserDTO userDTO = userService.createUser(createUserRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("User created successfully")
                .result(userDTO)
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserDTO>> updateUser(@PathVariable("id") String id,
                                              @RequestBody UpdateUserRequest updateUserRequest) {
        UserDTO userDTO = userService.updateUser(id, updateUserRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("User updated successfully")
                .result(userDTO)
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> removeUserById(@PathVariable("id") String id) {
        userService.removeUserById(id);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("User deleted successfully - id: " + id)
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/my-info")
    public ResponseEntity<SuccessResponse<?>> getProfileInfo() {
        SuccessResponse<?> successResponse = userService.getProfileInfo();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
