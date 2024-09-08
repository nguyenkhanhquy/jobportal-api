package com.jobportal.api.dto.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {

    private String email;
    private String newPassword;
}
