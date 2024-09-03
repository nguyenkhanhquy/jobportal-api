package com.jobportal.api.service;

import com.jobportal.api.dto.request.IntrospectRequest;
import com.jobportal.api.dto.request.LoginRequest;
import com.jobportal.api.dto.request.RegisterRequest;
import com.jobportal.api.dto.response.ErrorResponse;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.dto.user.UserDTO;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.UserMapper;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Value("${jwt.signerkey}")
    private String SIGNER_KEY;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        // Tìm người dùng theo email
        User user = userRepository.findByEmail(loginRequest.getEmail());

        // Kiểm tra nếu người dùng không tồn tại hoặc mật khẩu không khớp
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = generateToken(loginRequest.getEmail());

            // Ghi log cho sự kiện đăng nhập thành công
            logger.info("User {} logged in successfully", loginRequest.getEmail());

            SuccessResponse<Map<String, Object>> successResponse = new SuccessResponse<>();
            successResponse.setMessage("Login successfully");
            successResponse.setResult(Map.of("token", token));
            // 200 : Success
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }

        // Ghi log cho sự kiện đăng nhập thất bại
        logger.warn("Failed login attempt for user {}", loginRequest.getEmail());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Invalid email or password");
        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        // 401 : Unauthorized — user chưa được xác thực và truy cập vào resource yêu cầu phải xác thực
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<?> introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        String token = introspectRequest.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Phân tích token
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Kiểm tra tính hợp lệ của chữ ký
        boolean verified = signedJWT.verify(verifier);

        // Lấy thời gian hết hạn từ claims
        Instant expiration = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();

        if (verified && Instant.now().isBefore(expiration)) {
            // Lấy payload từ JWTClaimsSet
            Map<String, Object> payloadData = signedJWT.getJWTClaimsSet().getClaims();

            // Tạo result với payload
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("payload", payloadData);

            SuccessResponse<Map<String, Object>> successResponse = new SuccessResponse<>();
            successResponse.setMessage("Token is valid");
            successResponse.setResult(resultData);

            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Invalid token");
            errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());

            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
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
        // Kiểm tra xem newPassword có hợp lệ không
        if (newPassword == null || newPassword.trim().isEmpty() || newPassword.length() < 8) {
            throw new CustomException(EnumException.INVALID_PASSWORD);
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            // 404: Not found — không tồn tại resource
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }
        // Mã hóa mật khẩu với Bcrypt
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(newPassword));
        User dbUser = userRepository.save(user);

        SuccessResponse<UserDTO> successResponse = new SuccessResponse<>();
        successResponse.setResult(userMapper.mapUserToUserDTO(dbUser));
        successResponse.setMessage("Reset password successfully");
        // 200 : Success
        return new ResponseEntity<>(successResponse, HttpStatus.valueOf(200));
    }

    @Override
    public String generateToken(String email) {
        // Tạo JWSHeader với "typ": "JWT" và thuật toán HS512
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS512)
                .type(JOSEObjectType.JWT)
                .build();

        // Tạo thời gian hiện tại
        Instant now = Instant.now();

        // Tạo thời gian hết hạn (1 giờ sau thời điểm hiện tại)
        Instant expiration = now.plus(1, ChronoUnit.HOURS);

        // Tạo JWTClaimsSet chứa các thông tin cần thiết
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("21110282.codes")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiration))
                .claim("customClaim", "custom")
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
