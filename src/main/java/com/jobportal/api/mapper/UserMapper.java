package com.jobportal.api.mapper;

import com.jobportal.api.dto.request.auth.RegisterRequest;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.isActive());
        userDTO.setRegistrationDate(user.getRegistrationDate());
        userDTO.setRole(user.getRole().getName());
        return userDTO;
    }

    public User mapRegisterRequestToUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        return user;
    }
}
