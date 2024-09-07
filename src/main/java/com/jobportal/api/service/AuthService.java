package com.jobportal.api.service;

import com.jobportal.api.dto.request.IntrospectRequest;
import com.jobportal.api.dto.request.LoginRequest;
import com.jobportal.api.dto.request.LogoutRequest;
import com.jobportal.api.dto.request.RegisterRequest;
import com.jobportal.api.model.user.User;
import com.nimbusds.jose.JOSEException;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface AuthService {

    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    ResponseEntity<?> logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;

    ResponseEntity<?> register(RegisterRequest registerRequest);

    ResponseEntity<?> resetPassword(String email, String newPassword);

    String generateToken(User user);
}
