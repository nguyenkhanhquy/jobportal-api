package com.jobportal.api.dto.request;

import lombok.Getter;

@Getter
public class ActivateAccountRequest {

    private String token;
    private int otp;
}
