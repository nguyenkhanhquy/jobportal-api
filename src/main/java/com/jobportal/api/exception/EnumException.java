package com.jobportal.api.exception;

import lombok.Getter;

@Getter
public enum EnumException {
    USER_NOT_FOUND(404, "User not found"),
    USER_EXISTED(409, "User already existed"),
    ;

    EnumException(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    private final int errorCode;
    private final String message;
}
