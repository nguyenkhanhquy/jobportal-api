package com.jobportal.api.exception;

import com.jobportal.api.dto.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleException(Exception e) {
        CommonResponse response = new CommonResponse();
        response.setError(true);
        response.setMessage("Uncategorized Exception: " + e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse> handleCustomException(CustomException e) {
        EnumException enumException = e.getEnumException();

        CommonResponse response = new CommonResponse();
        response.setError(true);
        response.setMessage(enumException.getMessage());

        return new ResponseEntity<>(response, HttpStatus.valueOf(enumException.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        CommonResponse response = new CommonResponse();
        response.setError(true);
        response.setMessage(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
