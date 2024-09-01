package com.jobportal.api.service;

import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.entity.user.User;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.UserConverter;
import com.jobportal.api.repository.UserRepository;
import com.jobportal.api.dto.request.LoginRequest;
import com.jobportal.api.dto.request.RegisterRequest;
import com.jobportal.api.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO selectUserById(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }
        return userConverter.userToUserDTO(user);
    }

    @Override
    public UserDTO resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return userConverter.userToUserDTO(user);
    }

    @Override
    public Boolean CheckEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public ApiResponse<UserDTO> login(LoginRequest loginRequest) {
        ApiResponse<UserDTO> response = new ApiResponse<>();

        // Tìm người dùng theo email
        User user = userRepository.findByEmail(loginRequest.getEmail());

        // Kiểm tra nếu người dùng không tồn tại hoặc mật khẩu không khớp
        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            response.setError(true);
            response.setMessage("Invalid email or password");
            return response;
        }

        // Nếu người dùng tồn tại và mật khẩu khớp
        response.setError(false);
        response.setMessage("Login successfully");
        UserDTO userDTO = userConverter.userToUserDTO(user);
        response.setResult(userDTO);

        return response;
    }

    @Override
    public ApiResponse<UserDTO> register(RegisterRequest registerRequest) {
        // Kiểm tra xem email đã tồn tại trong hệ thống chưa
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomException(EnumException.USER_EXISTED);
        }

        ApiResponse<UserDTO> response = new ApiResponse<>();

        User theUser = userConverter.registerRequestToUser(registerRequest);

        // Xử lý thông tin người dùng mới
        theUser.setId(0); // ID sẽ được thiết lập bởi cơ sở dữ liệu

        // Lưu người dùng vào cơ sở dữ liệu
        User dbUser = userRepository.save(theUser);

        // Tạo phản hồi thành công
        response.setError(false);
        response.setMessage("Register successfully");
        UserDTO userDTO = userConverter.userToUserDTO(dbUser);
        response.setResult(userDTO);

        return response;
    }
}
