package com.jobportal.api.service;

import com.jobportal.api.dto.request.CreateUserRequest;
import com.jobportal.api.dto.request.UpdateUserRequest;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.model.user.Role;
import com.jobportal.api.model.user.User;
import com.jobportal.api.mapper.UserMapper;
import com.jobportal.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
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

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Override
    public ResponseEntity<?> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Email : {}", authentication.getName());
        log.info("Role : {}", authentication.getAuthorities());

        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @PostAuthorize("returnObject.body.email == authentication.name")
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

        // Đặt role mặc định là USER
        user.setRole(Role.USER);

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
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email);

        UserDTO userDTO = userMapper.mapUserToUserDTO(currentUser);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


    @Override
    public Boolean CheckEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
