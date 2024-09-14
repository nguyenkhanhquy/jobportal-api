package com.jobportal.api.mapper;

import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.model.user.Role;
import com.jobportal.api.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role", target = "role", qualifiedByName = "mapRoleToString")
    UserDTO mapUserToUserDTO(User user);

    @Named("mapRoleToString")
    default String mapRoleToString(Role role) {
        return role != null ? role.getName() : null;
    }
}
