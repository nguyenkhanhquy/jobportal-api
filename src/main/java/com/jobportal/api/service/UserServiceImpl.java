package com.jobportal.api.service;

import com.jobportal.api.dto.profile.JobSeekerProfileDTO;
import com.jobportal.api.dto.request.user.CreateUserRequest;
import com.jobportal.api.dto.request.user.UpdateUserRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.model.profile.JobSeekerProfile;
import com.jobportal.api.model.user.User;
import com.jobportal.api.mapper.UserMapper;
import com.jobportal.api.repository.JobSeekerProfileRepository;
import com.jobportal.api.repository.RoleRepository;
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

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JobSeekerProfileRepository jobSeekerProfileRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Override
    public ResponseEntity<?> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("getName() -> Email : {}", authentication.getName());
        log.info("getAuthorities() -> Scope : {}", authentication.getAuthorities());

        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @PostAuthorize("returnObject.body.email == authentication.name or hasAuthority('SCOPE_ADMIN')")
    @Override
    public ResponseEntity<?> getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserDTO userDTO = userMapper.mapUserToUserDTO(user.get());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> createUser(CreateUserRequest createUserRequest) {
        User user = userMapper.mapCreateUserRequestToUser(createUserRequest);

        // Mã hóa mật khẩu với Bcrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Đặt trạng thái kích hoạt mặc định là false
        user.setActive(false);

        // Đặt thời gian đăng ký
        user.setRegistrationDate(Date.from(Instant.now()));

        // Đặt role mặc định là USER
        user.setRole(roleRepository.findByName("JOB_SEEKER"));

        // Lưu người dùng vào cơ sở dữ liệu
        User dbUser = userRepository.save(user);

        // Lưu hồ sơ người dùng vào cơ sở dữ liệu
        if (dbUser.getRole().getName().equals("JOB_SEEKER")) {
            jobSeekerProfileRepository.save(new JobSeekerProfile(dbUser, createUserRequest.getFullName()));
        }

        UserDTO userDTO = userMapper.mapUserToUserDTO(dbUser);

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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email);
    }

    @Override
    public ResponseEntity<?> getProfileInfo() {
        User user = getCurrentUser();

        if (user.getRole().getName().equals("JOB_SEEKER")) {
            JobSeekerProfile seeker = jobSeekerProfileRepository.findById(user.getId()).orElse(null);

            if (seeker == null) {
                return new ResponseEntity<>("Job Seeker Profile not found", HttpStatus.NOT_FOUND);
            }

            JobSeekerProfileDTO seekerDTO = JobSeekerProfileDTO.builder()
                    .email(user.getEmail())
                    .isActive(user.isActive())
                    .fullName(seeker.getFullName())
                    .address(seeker.getAddress())
                    .workExperience(seeker.getWorkExperience())
                    .build();

            SuccessResponse<JobSeekerProfileDTO> successResponse = new SuccessResponse<>();
            successResponse.setResult(seekerDTO);

            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } else {
            UserDTO userDTO = userMapper.mapUserToUserDTO(user);

            SuccessResponse<UserDTO> successResponse = new SuccessResponse<>();
            successResponse.setResult(userDTO);

            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }
    }
}
