package com.jobportal.api.mapper;

import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.name", target = "role")
    UserDTO mapUserToUserDTO(User user);
}
