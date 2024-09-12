package com.jobportal.api.dto.request.auth;

import lombok.Getter;

@Getter
public class ActivateAccountRequest {

    private String token;
    private int otp;
}
