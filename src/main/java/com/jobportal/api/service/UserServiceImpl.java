package com.jobportal.api.service;

import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.model.user.User;
import com.jobportal.api.mapper.UserMapper;
import com.jobportal.api.repository.UserRepository;
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
    public Boolean CheckEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
