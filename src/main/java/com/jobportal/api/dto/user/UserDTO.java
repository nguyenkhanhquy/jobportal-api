package com.jobportal.api.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jobportal.api.model.user.Role;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    private String id;
    private String email;
    private String fullName;
    private Role role;
}
