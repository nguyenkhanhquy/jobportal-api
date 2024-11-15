package com.jobportal.api.service;

import com.jobportal.api.dto.request.user.CreateUserRequest;
import com.jobportal.api.dto.request.user.UpdateUserRequest;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.UserMapper;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.JobSeekerProfileRepository;
import com.jobportal.api.repository.RecruiterProfileRepository;
import com.jobportal.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JobSeekerProfileRepository jobSeekerProfileRepository, RecruiterProfileRepository recruiterProfileRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Override
    public List<UserDTO> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("getName() -> Email : {}", authentication.getName());
        log.info("getAuthorities() -> Scope : {}", authentication.getAuthorities());

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::mapUserToUserDTO)
                .toList();
    }

    @PostAuthorize("returnObject.email == authentication.name or hasAuthority('SCOPE_ADMIN')")
    @Override
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(EnumException.USER_NOT_FOUND));
        return userMapper.mapUserToUserDTO(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Override
    public UserDTO createUser(CreateUserRequest createUserRequest) {
        User user = User.builder()
                .email(createUserRequest.getEmail())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .isActive(false)
                .registrationDate(Date.from(Instant.now()))
                .build();

        try {
            User dbUser = userRepository.save(user);

            return userMapper.mapUserToUserDTO(dbUser);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(EnumException.USER_EXISTED);
        }
    }

    @PostAuthorize("returnObject.email == authentication.name or hasAuthority('SCOPE_ADMIN')")
    @Override
    public UserDTO updateUser(String id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(EnumException.USER_NOT_FOUND));

        user.setEmail(updateUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));

        try {
            // Lưu user vào cơ sở dữ liệu
            User dbUser = userRepository.save(user);

            return userMapper.mapUserToUserDTO(dbUser);
        } catch (Exception e) {
            throw new CustomException(EnumException.USER_EXISTED);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Override
    public void removeUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(EnumException.USER_NOT_FOUND));

        if (user.getRole().getName().equals("JOB_SEEKER")) {
            jobSeekerProfileRepository.deleteById(id);
        } else if (user.getRole().getName().equals("RECRUITER")) {
            recruiterProfileRepository.deleteById(id);
        } else {
            throw new CustomException(EnumException.ADMIN_CANNOT_BE_DELETED);
        }

        // Xoá user khỏi cơ sở dữ liệu
        userRepository.deleteById(id);
    }
}
