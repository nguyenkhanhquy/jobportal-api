package com.jobportal.api.service;

import com.jobportal.api.dto.request.user.CreateUserRequest;
import com.jobportal.api.dto.request.user.UpdateUserRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.dto.user.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(String id);

    UserDTO createUser(CreateUserRequest createUserRequest);

    UserDTO updateUser(String id, UpdateUserRequest updateUserRequest);

    void removeUserById(String id);

    SuccessResponse<?> getProfileInfo();
}
