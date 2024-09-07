package com.jobportal.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EnumException {
    // 400 : Bad request — dữ liệu gửi lên không hợp lệ
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Password must not be blank and password must be at least 8 characters"),
    // 401 : Unauthorized — user chưa được xác thực và truy cập vào resource yêu cầu phải xác thực
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "You are not authenticated"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    // 403: Forbidden — user không có quyền truy cập vào resource
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "You do not permission"),
    // 404: Not found — không tồn tại resource
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    // 409: Conflict
    USER_EXISTED(HttpStatus.CONFLICT, "User already existed"),
    // 500: Internal Server Error — có lỗi xẩy ra trong hệ thống
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized Exception: ")
    ;

    EnumException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    private final HttpStatus statusCode;
    private final String message;
}
