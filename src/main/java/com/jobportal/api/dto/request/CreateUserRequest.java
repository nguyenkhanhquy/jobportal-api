package com.jobportal.api.dto.request;

import lombok.Getter;

@Getter
public class CreateUserRequest {

    private String fullName;
    private String email;
    private String password;
}
