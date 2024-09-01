package com.jobportal.api.controller;

import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.dto.request.LoginRequest;
import com.jobportal.api.dto.request.RegisterRequest;
import com.jobportal.api.dto.response.ApiResponse;
import com.jobportal.api.dto.response.CommonResponse;
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
    public ResponseEntity<ApiResponse<UserDTO>> login(@Valid @RequestBody LoginRequest loginRequest) {
        ApiResponse<UserDTO> response = userService.login(loginRequest);

        if (response.isError()) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        ApiResponse<UserDTO> response = userService.register(registerRequest);

        if (response.isError()) {
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<CommonResponse> forgotPassword(@RequestParam("email") String email) {
        if (userService.CheckEmailExists(email)) {
            int otp = otpService.generateOtp(email);
            emailService.sendSimpleEmail(email, "Your OTP Code", "Your OTP Code is: " + otp);

            CommonResponse response = new CommonResponse();
            response.setError(false);
            response.setMessage("OTP send to your email");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<CommonResponse> validateOtp(@RequestParam("email") String email,
                                                      @RequestParam("otp") int otp) {
        CommonResponse response = new CommonResponse();
        if (otpService.validateOtp(email, otp)) {
            response.setError(false);
            response.setMessage("OTP is valid. You can now reset your password");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setError(true);
            response.setMessage("Invalid OTP or OTP expired");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
