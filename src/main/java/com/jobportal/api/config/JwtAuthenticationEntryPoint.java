package com.jobportal.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.api.dto.response.ErrorResponse;
import com.jobportal.api.exception.EnumException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        EnumException enumException = EnumException.UNAUTHENTICATED;

        response.setStatus(enumException.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(enumException.getMessage())
                .statusCode(enumException.getStatusCode().value())
                .build();

        writeResponse(response, errorResponse);
    }

    private void writeResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.flushBuffer();
    }
}
