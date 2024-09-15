package com.jobportal.api.controller;

import com.jobportal.api.dto.request.auth.*;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/v0/auth")
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

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping("/introspect")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> introspect(@RequestBody IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        Map<String, Object> resultData = authService.introspect(introspectRequest);

        SuccessResponse<Map<String, Object>> successResponse = SuccessResponse.<Map<String, Object>>builder()
                .message("Token is valid")
                .result(resultData)
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<Void>> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authService.logout(logoutRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Logout successfully")
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        Map<String, Object> resultData = authService.refreshToken(refreshTokenRequest);

        SuccessResponse<Map<String, Object>> successResponse = SuccessResponse.<Map<String, Object>>builder()
                .message("Refresh token successfully")
                .result(resultData)
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDTO userDTO = authService.register(registerRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Register successfully")
                .result(userDTO)
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<SuccessResponse<Void>> sendOtp(@RequestBody SendOtpRequest sendOtpRequest) {
        authService.sendOtp(sendOtpRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("OTP send to your email")
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<SuccessResponse<UserDTO>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        UserDTO userDTO = authService.resetPassword(resetPasswordRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Reset password successfully")
                .result(userDTO)
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping("/activate")
    public ResponseEntity<SuccessResponse<UserDTO>> activateAccount(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
                                             @Valid @RequestBody ActivateAccountRequest activateAccountRequest) throws ParseException, JOSEException {
        UserDTO userDTO = authService.activateAccount(authorizationHeader, activateAccountRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Activate account successfully")
                .result(userDTO)
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping("/update-password")
    public ResponseEntity<SuccessResponse<UserDTO>> activateAccount(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
                                                                    @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) throws ParseException, JOSEException {
        UserDTO userDTO = authService.updatePassword(authorizationHeader, updatePasswordRequest);

        SuccessResponse<UserDTO> successResponse = SuccessResponse.<UserDTO>builder()
                .message("Update password successfully")
                .result(userDTO)
                .build();

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
