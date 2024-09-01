package com.jobportal.api.service;

import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.entity.user.User;
import com.jobportal.api.repository.UserRepository;
import com.jobportal.api.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Boolean CheckEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserResponse checkRegister(User theUser) {
        UserResponse response = new UserResponse();

        // Kiểm tra các trường bắt buộc có null không
        if (theUser.getEmail().isEmpty() || theUser.getFullName().isEmpty() || theUser.getPassword().isEmpty()) {
            response.setError(true);
            response.setMessage("Invalid user: Email, full name, and password must not be empty.");
            return response;
        }

        // Kiểm tra xem email đã tồn tại trong hệ thống chưa
        boolean emailExists = userRepository.existsByEmail(theUser.getEmail());

        if (emailExists) {
            throw new RuntimeException("Email already exists");
        }

        return response;
    }

    @Override
    public UserResponse login(String email, String password) {
        UserResponse response = new UserResponse();

        // Tìm người dùng theo email
        User user = userRepository.findByEmail(email);

        // Kiểm tra nếu người dùng không tồn tại hoặc mật khẩu không khớp
        if (user == null || !user.getPassword().equals(password)) {
            response.setError(true);
            response.setMessage("Invalid email or password");
            return response;
        }

        // Nếu người dùng tồn tại và mật khẩu khớp
        response.setError(false);
        response.setMessage("Login successfully");
        UserDTO userDTO = UserDTO.fromUser(user);
        response.setUserDTO(userDTO);

        return response;
    }

    @Override
    public UserResponse register(User theUser) {
        UserResponse response = checkRegister(theUser);

        // Nếu thông tin trùng lặp
        if (response.isError()) {
            return response;
        }

        // Xử lý thông tin người dùng mới
        theUser.setId(0); // ID sẽ được thiết lập bởi cơ sở dữ liệu

        // Lưu người dùng vào cơ sở dữ liệu
        User dbUser = userRepository.save(theUser);

        // Tạo phản hồi thành công
        response.setError(false);
        response.setMessage("Register successfully");
        UserDTO userDTO = UserDTO.fromUser(dbUser);
        response.setUserDTO(userDTO);

        return response;
    }
}
