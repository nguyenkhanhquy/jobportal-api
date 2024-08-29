package com.jobportal.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jobportal.api.dto.user.UserDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private boolean error;
    private String message;
    private UserDTO userDTO;
}
