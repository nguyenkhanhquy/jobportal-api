package com.jobportal.api.controller;

import com.jobportal.api.entity.user.User;
import com.jobportal.api.request.LoginRequest;
import com.jobportal.api.response.UserResponse;
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

    /**
     * Đăng nhập người dùng.
     *
     * @param loginRequest yêu cầu đăng nhập chứa email và mật khẩu
     * @return phản hồi đăng nhập
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        UserResponse response = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (response.isError()) {
            // Thông tin đăng nhập không chính xác, trả về phản hồi với mã trạng thái UNAUTHORIZED
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // Tạo phản hồi thành công
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Đăng ký người dùng mới.
     *
     * @param theUser đối tượng người dùng chứa thông tin đăng ký
     * @return phản hồi đăng ký
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody User theUser) {
        UserResponse response = userService.register(theUser);

        if (response.isError()) {
            // Thông tin trùng lặp, trả về phản hồi với mã trạng thái CONFLICT
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        // Tạo phản hồi thành công
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
