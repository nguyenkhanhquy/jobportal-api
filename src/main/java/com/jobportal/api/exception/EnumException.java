package com.jobportal.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EnumException {
    // 400 : Bad request — dữ liệu gửi lên không hợp lệ
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Dữ liệu yêu cầu không hợp lệ"),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "Tên file không hợp lệ"),
    USER_EXISTED(HttpStatus.BAD_REQUEST, "Người dùng đã tồn tại"),
    COMPANY_EXISTED(HttpStatus.BAD_REQUEST, "Công ty đã tồn tại"),
    JOB_POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìm thấy công việc"),

    // 401 : Unauthorized — user chưa được xác thực
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Chưa xác thực"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token không hợp lệ"),
    INVALID_OTP(HttpStatus.UNAUTHORIZED, "OTP không hợp lệ"),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "Email hoặc mật khẩu không đúng"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Mật khẩu hiện tại không đúng"),

    // 403: Forbidden — user không có quyền
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "Không có quyền truy cập"),
    ADMIN_CANNOT_BE_DELETED(HttpStatus.FORBIDDEN, "Không thể xóa tài khoản admin"),

    // 404: Not found — không tồn tại resource
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy trang"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Hồ sơ không tồn tại"),

    // 500: Internal Server Error — có lỗi trong hệ thống
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Ngoại lệ chưa phân loại: "),
    JWT_SIGNING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể ký JWT Token"),
    ERROR_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi tải file"),
    ;

    private final HttpStatus statusCode;
    private final String message;

    EnumException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
