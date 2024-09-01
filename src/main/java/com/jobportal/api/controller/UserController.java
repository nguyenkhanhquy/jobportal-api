package com.jobportal.api.controller;

import com.jobportal.api.entity.user.User;
import com.jobportal.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Điều khiển các yêu cầu liên quan đến người dùng.
 */
@RestController
@RequestMapping("/api/v0/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Lấy danh sách tất cả người dùng.
     *
     * @return danh sách người dùng
     */
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
