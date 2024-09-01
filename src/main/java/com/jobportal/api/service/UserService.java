package com.jobportal.api.service;

import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.entity.user.User;
import com.jobportal.api.dto.request.LoginRequest;
import com.jobportal.api.dto.request.RegisterRequest;
import com.jobportal.api.dto.response.ApiResponse;

import java.util.List;

public interface UserService {

    List<User> findAll();

    UserDTO selectUserById(Integer userId);

    UserDTO resetPassword(String email, String newPassword);

    Boolean CheckEmailExists(String email);

    ApiResponse<UserDTO> login(LoginRequest loginRequest);

    ApiResponse<UserDTO> register(RegisterRequest registerRequest);
}
