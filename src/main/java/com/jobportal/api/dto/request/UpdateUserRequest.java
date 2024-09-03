package com.jobportal.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    private String id;
    private String fullName;
    private String email;
    private String password;
}
