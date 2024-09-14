package com.jobportal.api.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ActivateAccountRequest {

    @NotBlank(message = "Otp is required")
    private String otp;
}
