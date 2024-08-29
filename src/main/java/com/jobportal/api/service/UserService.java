package com.jobportal.api.service;

import com.jobportal.api.entity.user.User;
import com.jobportal.api.response.UserResponse;

import java.util.List;

public interface UserService {

    List<User> findAll();

    UserResponse checkInfo(User theUser);

    UserResponse login(String userName, String password);

    UserResponse register(User theUser);
}
