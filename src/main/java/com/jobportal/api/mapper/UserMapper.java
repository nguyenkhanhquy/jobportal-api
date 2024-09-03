package com.jobportal.api.mapper;

import com.jobportal.api.dto.request.CreateUserRequest;
import com.jobportal.api.dto.request.RegisterRequest;
import com.jobportal.api.dto.request.UpdateUserRequest;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    public User mapCreateUserRequestToUser(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setFullName(createUserRequest.getFullName());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        return user;
    }

    public User mapUpdateUserRequestToUser(UpdateUserRequest updateUserRequest) {
        User user = new User();
        user.setId(updateUserRequest.getId());
        user.setFullName(updateUserRequest.getFullName());
        user.setEmail(updateUserRequest.getEmail());
        user.setPassword(updateUserRequest.getPassword());
        return user;
    }

    public User mapRegisterRequestToUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        return user;
    }
}
