package com.jobportal.api.controller;

import com.jobportal.api.dto.response.ErrorResponse;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.dto.request.LoginRequest;
import com.jobportal.api.dto.request.RegisterRequest;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        if (userService.CheckEmailExists(email)) {
            int otp = otpService.generateOtp(email);
            emailService.sendSimpleEmail(email, "Your OTP Code", "Your OTP Code is: " + otp);

            SuccessResponse<?> successResponse = new SuccessResponse<>();
            successResponse.setMessage("OTP send to your email");
            // 200 : Success
            return new ResponseEntity<>(successResponse, HttpStatus.valueOf(200));
        }
        else {
            // 404: Not found — không tồn tại resource
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestParam("email") String email, @RequestParam("otp") int otp) {
        if (otpService.validateOtp(email, otp)) {
            SuccessResponse<?> successResponse = new SuccessResponse<>();
            successResponse.setMessage("OTP is valid. You can now reset your password");
            // 200 : Success
            return new ResponseEntity<>(successResponse, HttpStatus.valueOf(200));
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Invalid OTP or OTP expired");
            errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            // 401 : Unauthorized — user chưa được xác thực và truy cập vào resource yêu cầu phải xác thực
            return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(401));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword) {
        return userService.resetPassword(email, newPassword);
    }
}
