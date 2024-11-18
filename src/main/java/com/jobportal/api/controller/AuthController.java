package com.jobportal.api.controller;

import com.jobportal.api.dto.request.auth.*;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, Object> resultData = authService.login(loginRequest);

        SuccessResponse<Map<String, Object>> successResponse = SuccessResponse.<Map<String, Object>>builder()
                .message("Đăng nhập thành công")
                .result(resultData)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/introspect")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> introspect(@RequestBody IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        Map<String, Object> resultData = authService.introspect(introspectRequest);

        SuccessResponse<Map<String, Object>> successResponse = SuccessResponse.<Map<String, Object>>builder()
                .message("Token is valid")
                .result(resultData)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<Void>> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authService.logout(logoutRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Đăng xuất thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        Map<String, Object> resultData = authService.refreshToken(refreshTokenRequest);

        SuccessResponse<Map<String, Object>> successResponse = SuccessResponse.<Map<String, Object>>builder()
                .message("Refresh token successfully")
                .result(resultData)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/register/job-seeker")
    public ResponseEntity<SuccessResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDTO userDTO = authService.registerJobSeeker(registerRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Đăng ký tài khoản thành công")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<SuccessResponse<UserDTO>> registerRecruiter(@Valid @RequestBody RegisterRecruiterRequest registerRecruiterRequest) {
        UserDTO userDTO = authService.registerRecruiter(registerRecruiterRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Đăng ký tài khoản thành công")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<SuccessResponse<Void>> sendOtp(@RequestBody SendOtpRequest sendOtpRequest) {
        authService.sendOtp(sendOtpRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("OTP đã gửi đến email của bạn")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<SuccessResponse<UserDTO>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        UserDTO userDTO = authService.resetPassword(resetPasswordRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Đặt lại mật khẩu thành công")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/activate")
    public ResponseEntity<SuccessResponse<Void>> activateAccount(@RequestBody Map<String, String> request) {
        authService.activateAccount(request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Đăng ký tài khoản thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/update-password")
    public ResponseEntity<SuccessResponse<UserDTO>> activateAccount(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        UserDTO userDTO = authService.updatePassword(updatePasswordRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Cập nhật mật khẩu thành công")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<UserDTO>> getCurrentAuthUser() {
        UserDTO userDTO = authService.getCurrentAuthUser();

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Get current auth user successfully")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<SuccessResponse<Object>> getCurrentAuthProfile() {
        SuccessResponse<Object> successResponse = SuccessResponse.builder()
                .message("Get current auth profile successfully")
                .result(authService.getCurentAuthProfile())
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
