package com.jobportal.api.service;

import com.jobportal.api.dto.response.ErrorResponse;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.entity.user.User;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.UserMapper;
import com.jobportal.api.repository.UserRepository;
import com.jobportal.api.dto.request.LoginRequest;
import com.jobportal.api.dto.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity<?> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user, HttpStatus.valueOf(200));
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> createUser(UserDTO userDTO) {
        User user = userMapper.mapUserDTOToUser(userDTO);
        user.setId(0L); // cast argument to 'long' 0 -> 0L
        // Mã hóa mật khẩu với Bcrypt
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity<?> updateUser(UserDTO userDTO) {
        User user = userMapper.mapUserDTOToUser(userDTO);
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity<?> removeUserById(Long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        // Tìm người dùng theo email
        User user = userRepository.findByEmail(loginRequest.getEmail());

        // Kiểm tra nếu người dùng không tồn tại hoặc mật khẩu không khớp
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            SuccessResponse<UserDTO> response = new SuccessResponse<>();
            response.setResult(userMapper.mapUserToUserDTO(user));
            response.setMessage("Login successfully");
            // 200 : Success
            return new ResponseEntity<>(response, HttpStatus.valueOf(200));
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setMessage("Invalid email or password");
        // 401 : Unauthorized — user chưa được xác thực và truy cập vào resource yêu cầu phải xác thực
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(401));
    }

    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        // Kiểm tra xem email đã tồn tại trong hệ thống chưa
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomException(EnumException.USER_EXISTED);
        }

        // Lưu người dùng vào cơ sở dữ liệu
        User user = userMapper.registerRequestToUser(registerRequest);
        user.setId(0L); // cast argument to 'long' 0 -> 0L
        // Mã hóa mật khẩu với Bcrypt
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User dbUser = userRepository.save(user);

        // Tạo phản hồi thành công
        SuccessResponse<UserDTO> successResponse = new SuccessResponse<>();
        UserDTO userDTO = userMapper.mapUserToUserDTO(dbUser);
        successResponse.setResult(userDTO);
        successResponse.setMessage("Register successfully");
        // 200 : Success
        return new ResponseEntity<>(successResponse, HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity<?> resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // 404: Not found — không tồn tại resource
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        // 200 : Success
        return new ResponseEntity<>(userMapper.mapUserToUserDTO(user), HttpStatus.valueOf(200));
    }

    @Override
    public Boolean CheckEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
