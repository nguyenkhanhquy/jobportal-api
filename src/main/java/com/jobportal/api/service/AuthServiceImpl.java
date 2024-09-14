package com.jobportal.api.service;

import com.jobportal.api.dto.request.auth.*;
import com.jobportal.api.dto.response.SuccessResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.signerkey}")
    private String SIGNER_KEY;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final OtpService otpService;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JobSeekerProfileRepository jobSeekerProfileRepository, InvalidatedTokenRepository invalidatedTokenRepository, OtpService otpService, EmailService emailService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
        this.otpService = otpService;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public Map<String, Object> login(LoginRequest loginRequest) {
        // Tìm user theo email
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = generateToken(user);

            return Map.of("token", token);
        } else {
            throw new CustomException(EnumException.INVALID_LOGIN);
        }
    }

    @Override
    public Map<String, Object> introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        String token = introspectRequest.getToken();

        try {
            SignedJWT signedJWT = verifyToken(token);

            // Lấy payload từ JWTClaimsSet
            Map<String, Object> payloadData = signedJWT.getJWTClaimsSet().getClaims();

            // Tạo result với payload
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("payload", payloadData);

            return resultData;
        } catch (CustomException e) {
            throw new CustomException(EnumException.INVALID_TOKEN);
        }
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        String token = logoutRequest.getToken();

        SignedJWT signedJWT = verifyToken(token);

        InvalidatedToken invalidatedToken = createInvalidatedToken(signedJWT);

        invalidatedTokenRepository.save(invalidatedToken);
    }

    @Override
    public ResponseEntity<?> refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        String token = refreshTokenRequest.getToken();

        SignedJWT signedJWT = verifyToken(token);

        InvalidatedToken invalidatedToken = createInvalidatedToken(signedJWT);

        invalidatedTokenRepository.save(invalidatedToken);

        String email = signedJWT.getJWTClaimsSet().getSubject();

        // Tìm người dùng theo email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.UNAUTHENTICATED);
        }

        String newToken = generateToken(user);

        SuccessResponse<Map<String, Object>> successResponse = new SuccessResponse<>();
        successResponse.setMessage("Refresh token successfully");
        successResponse.setResult(Map.of("token", newToken));
        // 200 : Success
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Phân tích token
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Kiểm tra tính hợp lệ của chữ ký
        boolean verified = signedJWT.verify(verifier);

        // Lấy thời gian hết hạn từ claims
        Instant expiration = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();

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

    @Override
    public UserDTO register(RegisterRequest registerRequest) {
        // Kiểm tra xem email đã tồn tại trong hệ thống chưa
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomException(EnumException.USER_EXISTED);
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isActive(false)
                .registrationDate(Date.from(Instant.now()))
                .role(roleRepository.findByName("JOB_SEEKER"))
                .build();

        // Lưu user vào cơ sở dữ liệu
        User dbUser = userRepository.save(user);

        // Lưu hồ sơ vào cơ sở dữ liệu
        if (dbUser.getRole().getName().equals("JOB_SEEKER")) {
            jobSeekerProfileRepository.save(new JobSeekerProfile(dbUser, registerRequest.getFullName()));
        }

        return userMapper.mapUserToUserDTO(dbUser);
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
    public UserDTO activateAccount(String authorizationHeader, ActivateAccountRequest activateAccountRequest) throws ParseException, JOSEException {
        // Lấy token từ header Authorization
        String token = getTokenAuthorization(authorizationHeader);

        // Kiểm tra token
        SignedJWT signedJWT = verifyToken(token);

        // Lấy email từ Claims
        String email = signedJWT.getJWTClaimsSet().getSubject();

        // Kiểm tra otp
        verifyOtp(email, activateAccountRequest.getOtp());

        // Tìm user theo email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        // Kích hoạt tài khoản của user
        user.setActive(true);

        // Lưu user vào cơ sở dữ liệu
        User dbUser = userRepository.save(user);

        return userMapper.mapUserToUserDTO(dbUser);
    }

    @Override
    public UserDTO updatePassword(String authorizationHeader, UpdatePasswordRequest updatePasswordRequest) throws ParseException, JOSEException {
        // Lấy token từ header Authorization
        String token = getTokenAuthorization(authorizationHeader);

        // Kiểm tra token
        SignedJWT signedJWT = verifyToken(token);

        // Lấy email từ Claims
        String email = signedJWT.getJWTClaimsSet().getSubject();

        // Tìm user theo email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(updatePasswordRequest.getPassword(), user.getPassword())) {
            throw new CustomException(EnumException.INVALID_PASSWORD);
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));

        // Lưu user vào cơ sở dữ liệu
        User dbUser = userRepository.save(user);

        return userMapper.mapUserToUserDTO(dbUser);
    }

    private String getTokenAuthorization(String authorizationHeader) {
        // Kiểm tra xem header Authorization có tồn tại hay không
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(EnumException.UNAUTHENTICATED);
        }

        // Lấy token từ header Authorization
        return authorizationHeader.substring(7); // Loại bỏ phần "Bearer " để lấy token
    }

    private String generateToken(User user) {
        // Tạo JWSHeader với "typ": "JWT" và "alg": "HS512"
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS512)
                .type(JOSEObjectType.JWT)
                .build();

        // Tạo thời gian hiện tại
        Instant now = Instant.now();
        // Tạo thời gian hết hạn (1 giờ sau thời điểm hiện tại)
        Instant expiration = now.plus(1, ChronoUnit.HOURS);

        // Tạo JWTClaimsSet chứa các thông tin cần thiết
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("21110282.codes")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", user.getRole().getName())
                .build();

        // Chuyển đổi JWTClaimsSet thành payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Tạo JWSObject với header và payload
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            // Ký JWSObject với khóa ký bí mật
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            // Trả về JWT đã ký dưới dạng chuỗi
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to sign the JWT token", e);
        }
    }
}
