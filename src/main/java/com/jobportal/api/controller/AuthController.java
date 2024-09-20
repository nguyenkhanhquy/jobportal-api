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
                .message("Login successfully")
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
                .message("Logout successfully")
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

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDTO userDTO = authService.register(registerRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Register successfully")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<SuccessResponse<Void>> sendOtp(@RequestBody SendOtpRequest sendOtpRequest) {
        authService.sendOtp(sendOtpRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("OTP send to your email")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<SuccessResponse<UserDTO>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        UserDTO userDTO = authService.resetPassword(resetPasswordRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Reset password successfully")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/activate")
    public ResponseEntity<SuccessResponse<UserDTO>> activateAccount(@Valid @RequestBody ActivateAccountRequest activateAccountRequest) {
        UserDTO userDTO = authService.activateAccount(activateAccountRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Activate account successfully")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/update-password")
    public ResponseEntity<SuccessResponse<UserDTO>> activateAccount(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        UserDTO userDTO = authService.updatePassword(updatePasswordRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Update password successfully")
                .result(userDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
