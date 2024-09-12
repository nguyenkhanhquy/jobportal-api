package com.jobportal.api.service;

import com.jobportal.api.dto.request.auth.*;
import com.nimbusds.jose.JOSEException;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface AuthService {

    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    ResponseEntity<?> logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;

    ResponseEntity<?> refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException;

    ResponseEntity<?> register(RegisterRequest registerRequest);

    ResponseEntity<?> sendOtp(SendOtpRequest sendOtpRequest);

    ResponseEntity<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    ResponseEntity<?> validateOtp(ValidateOtpRequest validateOtpRequest);

    ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest);

    ResponseEntity<?> activateAccount(ActivateAccountRequest activateAccountRequest, String authorizationHeader) throws ParseException, JOSEException;
}
