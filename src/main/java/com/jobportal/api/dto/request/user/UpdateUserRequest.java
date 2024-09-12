package com.jobportal.api.dto.request.user;

import lombok.Getter;

@Getter
public class UpdateUserRequest {

    private String id;
    private String fullName;
    private String email;
    private String password;
}
