package com.jobportal.api.dto.request;

import lombok.Getter;

@Getter
public class ValidateOtpRequest {

    private String email;
    private int otp;
}
