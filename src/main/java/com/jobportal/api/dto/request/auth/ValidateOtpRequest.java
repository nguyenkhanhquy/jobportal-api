package com.jobportal.api.dto.request.auth;

import lombok.Getter;

@Getter
public class ValidateOtpRequest {

    private String email;
    private int otp;
}
