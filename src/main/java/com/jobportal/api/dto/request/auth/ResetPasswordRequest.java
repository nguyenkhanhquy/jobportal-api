package com.jobportal.api.dto.request.auth;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {

    private String email;
    private String newPassword;
}
