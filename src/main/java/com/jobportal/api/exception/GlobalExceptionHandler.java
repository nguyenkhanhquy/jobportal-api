package com.jobportal.api.exception;

import com.jobportal.api.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        EnumException enumException = EnumException.UNCATEGORIZED_EXCEPTION;

        ErrorResponse response = ErrorResponse.builder()
                .message(enumException.getMessage() + e.getMessage())
                .statusCode(enumException.getStatusCode().value())
                .build();

        return new ResponseEntity<>(response, enumException.getStatusCode());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        EnumException enumException = e.getEnumException();

        ErrorResponse response = ErrorResponse.builder()
                .message(enumException.getMessage())
                .statusCode(enumException.getStatusCode().value())
                .build();

        return new ResponseEntity<>(response, enumException.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ErrorResponse response = ErrorResponse.builder()
                .message(String.join(", ", errorMessages))
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AuthorizationDeniedException e) {
        EnumException enumException = EnumException.UNAUTHORIZED;

        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage() + " - " + enumException.getMessage())
                .statusCode(enumException.getStatusCode().value())
                .build();

        return new ResponseEntity<>(response, enumException.getStatusCode());
    }
}
