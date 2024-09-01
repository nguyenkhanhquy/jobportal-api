package com.jobportal.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jobportal.api.dto.user.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private boolean error;
    private String message;
    private UserDTO userDTO;
}
