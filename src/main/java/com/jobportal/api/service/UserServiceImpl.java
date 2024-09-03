package com.jobportal.api.service;

import com.jobportal.api.dto.request.CreateUserRequest;
import com.jobportal.api.dto.request.UpdateUserRequest;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.model.user.User;
import com.jobportal.api.mapper.UserMapper;
import com.jobportal.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserDTO userDTO = userMapper.mapUserToUserDTO(user.get());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> createUser(CreateUserRequest createUserRequest) {
        User user = userMapper.mapCreateUserRequestToUser(createUserRequest);

        // Mã hóa mật khẩu với Bcrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        UserDTO userDTO = userMapper.mapUserToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateUser(UpdateUserRequest updateUserRequest) {
        User user = userMapper.mapUpdateUserRequestToUser(updateUserRequest);

        // Mã hóa mật khẩu với Bcrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        UserDTO userDTO = userMapper.mapUserToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeUserById(String id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @Override
    public Boolean CheckEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
