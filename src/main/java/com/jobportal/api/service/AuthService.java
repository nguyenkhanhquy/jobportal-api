package com.jobportal.api.service;

import com.jobportal.api.dto.request.auth.*;
import com.jobportal.api.dto.user.UserDTO;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;
import java.util.Map;

public interface AuthService {

    Map<String, Object> login(LoginRequest loginRequest);

    Map<String, Object> introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;

    Map<String, Object> refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException;

    UserDTO register(RegisterRequest registerRequest);

    void sendOtp(SendOtpRequest sendOtpRequest);

    UserDTO resetPassword(ResetPasswordRequest resetPasswordRequest);

    UserDTO activateAccount(ActivateAccountRequest activateAccountRequest);

    UserDTO updatePassword(UpdatePasswordRequest updatePasswordRequest);
}
