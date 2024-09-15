package com.jobportal.api.dto.request.user;

import lombok.Getter;

@Getter
public class UpdateUserRequest {

    private String email;

    private String password;
}
