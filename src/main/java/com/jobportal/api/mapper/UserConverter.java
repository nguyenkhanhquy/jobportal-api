package com.jobportal.api.mapper;

import com.jobportal.api.dto.request.RegisterRequest;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    public User registerRequestToUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        return user;
    }
}
