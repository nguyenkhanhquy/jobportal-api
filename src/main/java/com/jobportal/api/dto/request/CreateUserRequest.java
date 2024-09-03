package com.jobportal.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    private String fullName;
    private String email;
    private String password;
}
