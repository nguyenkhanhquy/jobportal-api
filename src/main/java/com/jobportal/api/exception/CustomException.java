package com.jobportal.api.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private final EnumException enumException;

    public CustomException(EnumException enumException) {
        super(enumException.getMessage());
        this.enumException = enumException;
    }
}
