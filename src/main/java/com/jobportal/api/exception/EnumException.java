package com.jobportal.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EnumException {
    // 400 : Bad request — dữ liệu gửi lên không hợp lệ
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request data"),
    USER_EXISTED(HttpStatus.BAD_REQUEST, "User already existed"),

    // 401 : Unauthorized — user chưa được xác thực và truy cập vào resource yêu cầu phải xác thực
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "You are not authenticated"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    INVALID_OTP(HttpStatus.UNAUTHORIZED, "Invalid OTP or OTP expired"),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "Invalid email or password"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Current password is incorrect"),

    // 403: Forbidden — user không có quyền truy cập vào resource
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "You do not permission"),
    ADMIN_CANNOT_BE_DELETED(HttpStatus.FORBIDDEN, "Cannot delete user with ADMIN role"),

    // 404: Not found — không tồn tại resource
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Profile not found"),

    // 500: Internal Server Error — có lỗi xẩy ra trong hệ thống
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized Exception: "),
    JWT_SIGNING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to sign the JWT token"),
    ;

    EnumException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    private final HttpStatus statusCode;
    private final String message;
}
