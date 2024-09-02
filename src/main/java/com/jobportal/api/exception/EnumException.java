package com.jobportal.api.exception;

import lombok.Getter;

@Getter
public enum EnumException {
    // 404: Not found — không tồn tại resource
    USER_NOT_FOUND(404, "User not found"),
    // 409: Conflict
    USER_EXISTED(409, "User already existed"),
    ;

    EnumException(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    private final int errorCode;
    private final String message;
}
