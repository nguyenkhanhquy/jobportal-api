package com.jobportal.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EnumException {
    // 400 : Bad request — dữ liệu gửi lên không hợp lệ
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request data"),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "Invalid file name"),
    USER_EXISTED(HttpStatus.BAD_REQUEST, "User already existed"),
    COMPANY_EXISTED(HttpStatus.BAD_REQUEST, "Company already existed"),

    // 401 : Unauthorized — user chưa được xác thực
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "You are not authenticated"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    INVALID_OTP(HttpStatus.UNAUTHORIZED, "Invalid OTP or OTP expired"),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "Invalid email or password"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Current password is incorrect"),

    // 403: Forbidden — user không có quyền
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "You do not have permission"),
    ADMIN_CANNOT_BE_DELETED(HttpStatus.FORBIDDEN, "Cannot delete user with ADMIN role"),

    // 404: Not found — không tồn tại resource
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Page not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Profile not found"),

    // 500: Internal Server Error — có lỗi trong hệ thống
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized Exception: "),
    JWT_SIGNING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to sign the JWT token"),
    ERROR_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file"),
    ;

    private final HttpStatus statusCode;
    private final String message;

    EnumException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
