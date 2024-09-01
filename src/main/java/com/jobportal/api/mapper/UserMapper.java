package com.jobportal.api.mapper;

import com.jobportal.api.dto.request.RegisterRequest;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.entity.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User registerRequestToUser(RegisterRequest registerRequest);
    UserDTO userToUserDTO(User user);
}
