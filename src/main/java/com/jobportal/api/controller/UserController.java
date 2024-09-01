package com.jobportal.api.controller;

import com.jobportal.api.entity.user.User;
import com.jobportal.api.dto.response.ApiResponse;
import com.jobportal.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> findAll() {
        List<User> users = userService.findAll();
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(users);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
