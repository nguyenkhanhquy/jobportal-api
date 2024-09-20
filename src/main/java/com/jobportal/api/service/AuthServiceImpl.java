package com.jobportal.api.service;

import com.jobportal.api.dto.request.auth.*;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.UserMapper;
import com.jobportal.api.model.profile.JobSeekerProfile;
import com.jobportal.api.model.user.InvalidatedToken;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.InvalidatedTokenRepository;
import com.jobportal.api.repository.JobSeekerProfileRepository;
import com.jobportal.api.repository.RoleRepository;
import com.jobportal.api.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.signerkey}")
    private String jwtSignerKey;

    @Value("${jwt.valid-duration}")
    private int jwtValidDuration;

    @Value("${jwt.refreshable-duration}")
    private int jwtRefreshableDuration;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final OtpService otpService;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JobSeekerProfileRepository jobSeekerProfileRepository, InvalidatedTokenRepository invalidatedTokenRepository, OtpService otpService, EmailService emailService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
        this.otpService = otpService;
        this.emailService = emailService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Map<String, Object> login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(EnumException.INVALID_LOGIN);
        }

        String token = generateToken(user);
        return Map.of("token", token);
    }

    @Override
    public Map<String, Object> introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        String token = introspectRequest.getToken();

        try {
            SignedJWT signedJWT = verifyToken(token, false);

            // Lấy payload từ JWTClaimsSet
            Map<String, Object> payloadData = signedJWT.getJWTClaimsSet().getClaims();

            return Map.of("payload", payloadData);
        } catch (CustomException e) {
            throw new CustomException(EnumException.INVALID_TOKEN);
        }
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        String token = logoutRequest.getToken();

        SignedJWT signedJWT = verifyToken(token, false);
        invalidatedTokenRepository.save(createInvalidatedToken(signedJWT));
    }

    @Override
    public  Map<String, Object> refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        String token = refreshTokenRequest.getToken();

        SignedJWT signedJWT = verifyToken(token, true);
        invalidatedTokenRepository.save(createInvalidatedToken(signedJWT));

        String email = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.UNAUTHENTICATED);
        }

        String newToken = generateToken(user);
        return Map.of("token", newToken);
    }

    @Override
    public UserDTO register(RegisterRequest registerRequest) {
        // Tạo user mới
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isActive(false)
                .registrationDate(Date.from(Instant.now()))
                .role(roleRepository.findByName("JOB_SEEKER"))
                .build();

        try {
            // Lưu user vào cơ sở dữ liệu
            User dbUser = userRepository.save(user);

            // Lưu hồ sơ vào cơ sở dữ liệu
            if (dbUser.getRole().getName().equals("JOB_SEEKER")) {
                jobSeekerProfileRepository.save(new JobSeekerProfile(dbUser, registerRequest.getFullName()));
            }

            return userMapper.mapUserToUserDTO(dbUser);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(EnumException.USER_EXISTED);
        }
    }

    @Override
    public void sendOtp(SendOtpRequest sendOtpRequest) {
        String email = sendOtpRequest.getEmail();

        if (!userRepository.existsByEmail(email)) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        int otp = otpService.generateOtp(email);
        emailService.sendSimpleEmail(email, "Your OTP Code", "Your OTP Code is: " + otp);
    }

    private void verifyOtp(String email, String otpString) {
        try {
            int otp = Integer.parseInt(otpString);

            if (!otpService.validateOtp(email, otp)) {
                throw new CustomException(EnumException.INVALID_OTP);
            }
        } catch (NumberFormatException e) {
            throw new CustomException(EnumException.INVALID_OTP);
        }
    }

    @Override
    public UserDTO resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String email = resetPasswordRequest.getEmail();
        String newPassword = resetPasswordRequest.getNewPassword();

        verifyOtp(email, resetPasswordRequest.getOtp());

        // Tìm user theo email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        // Mã hóa mật khẩu với Bcrypt
        user.setPassword(passwordEncoder.encode(newPassword));

        // Lưu user vào cơ sở dữ liệu
        User dbUser = userRepository.save(user);

        return userMapper.mapUserToUserDTO(dbUser);
    }

    @Override
    public UserDTO activateAccount(ActivateAccountRequest activateAccountRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(EnumException.UNAUTHENTICATED);
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        verifyOtp(email, activateAccountRequest.getOtp());

        user.setActive(true);
        User dbUser = userRepository.save(user);

        return userMapper.mapUserToUserDTO(dbUser);
    }

    @Override
    public UserDTO updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(EnumException.UNAUTHENTICATED);
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(updatePasswordRequest.getPassword(), user.getPassword())) {
            throw new CustomException(EnumException.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        User dbUser = userRepository.save(user);

        return userMapper.mapUserToUserDTO(dbUser);
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(jwtSignerKey.getBytes());

        // Phân tích token
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Kiểm tra tính hợp lệ của chữ ký
        boolean verified = signedJWT.verify(verifier);

        // Lấy thời gian hết hạn hoặc thời gian làm mới từ claims
        Instant expiration = (isRefresh)
                ? signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(jwtRefreshableDuration, ChronoUnit.SECONDS)
                : signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();

        if (!(verified && Instant.now().isBefore(expiration))) {
            throw new CustomException(EnumException.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new CustomException(EnumException.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private InvalidatedToken createInvalidatedToken(SignedJWT signedJWT) throws ParseException {
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
    }

    private String generateToken(User user) {
        // Tạo JWSHeader với "typ": "JWT" và "alg": "HS512"
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS512)
                .type(JOSEObjectType.JWT)
                .build();

        // Lấy thời gian hiện tại
        Instant now = Instant.now();
        // Tạo thời gian hết hạn (thời gian hiện tại + thời hạn token)
        Instant expiration = now.plus(jwtValidDuration, ChronoUnit.HOURS);

        // Tạo JWTClaimsSet chứa các thông tin cần thiết
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("21110282.codes")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", user.getRole().getName())
                .build();

        // Chuyển đổi JWTClaimsSet thành Payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Tạo JWSObject với Header và Payload
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            // Ký JWSObject với khóa ký bí mật
            jwsObject.sign(new MACSigner(jwtSignerKey.getBytes()));
            // Trả về JWT đã ký dưới dạng chuỗi
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new CustomException(EnumException.JWT_SIGNING_ERROR);
        }
    }
}
