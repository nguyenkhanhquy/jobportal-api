package com.jobportal.api.exception;

import com.jobportal.api.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(new CommonResponse(true, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
