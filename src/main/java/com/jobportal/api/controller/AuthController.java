package com.jobportal.api.controller;

import com.jobportal.api.entity.user.User;
import com.jobportal.api.request.LoginRequest;
import com.jobportal.api.request.RegisterRequest;
import com.jobportal.api.response.CommonResponse;
import com.jobportal.api.response.UserResponse;
import com.jobportal.api.service.EmailService;
import com.jobportal.api.service.OtpService;
import com.jobportal.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v0/auth")
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;

    @Autowired
    public AuthController(UserService userService, OtpService otpService, EmailService emailService) {
        this.userService = userService;
        this.otpService = otpService;
        this.emailService = emailService;
    }


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

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse response = userService.register(registerRequest);

        if (response.isError()) {
            // Thông tin trùng lặp, trả về phản hồi với mã trạng thái CONFLICT
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        // Tạo phản hồi thành công
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<CommonResponse> forgotPassword(@RequestParam("email") String email) {
        if (userService.CheckEmailExists(email)) {
            int otp = otpService.generateOtp(email);
            emailService.sendSimpleEmail(email, "Your OTP Code", "Your OTP Code is: " + otp);
            return new ResponseEntity<>(new CommonResponse(false, "OTP send to your email"), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new CommonResponse(true, "Email does not exist in the system"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<CommonResponse> validateOtp(@RequestParam("email") String email,
                                                      @RequestParam("otp") int otp) {
        if (otpService.validateOtp(email, otp)) {
            return new ResponseEntity<>(new CommonResponse(false, "OTP is valid. You can now reset your password"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CommonResponse(true, "Invalid OTP or OTP expired"), HttpStatus.UNAUTHORIZED);
        }
    }
}
