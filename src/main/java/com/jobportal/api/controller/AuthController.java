package com.jobportal.api.controller;

import com.jobportal.api.entity.user.User;
import com.jobportal.api.request.LoginRequest;
import com.jobportal.api.response.CommonResponse;
import com.jobportal.api.response.UserResponse;
import com.jobportal.api.service.EmailService;
import com.jobportal.api.service.OtpService;
import com.jobportal.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v0/auth")
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;

    @Autowired
    public AuthController(UserService userService, OtpService otpService, EmailService emailService) {
        this.userService = userService;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    /**
     * Đăng nhập người dùng vào hệ thống.
     *
     * @param loginRequest yêu cầu đăng nhập chứa email và mật khẩu
     * @return phản hồi đăng nhập bao gồm thông tin người dùng nếu thành công, hoặc thông báo lỗi nếu không thành công
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        UserResponse response = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (response.isError()) {
            // Thông tin đăng nhập không chính xác, trả về phản hồi với mã trạng thái UNAUTHORIZED
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // Tạo phản hồi thành công
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Đăng ký người dùng mới vào hệ thống.
     *
     * @param theUser đối tượng người dùng chứa thông tin đăng ký
     * @return phản hồi đăng ký bao gồm thông tin người dùng nếu thành công, hoặc thông báo lỗi nếu không thành công
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody User theUser) {
        UserResponse response = userService.register(theUser);

        if (response.isError()) {
            // Thông tin trùng lặp, trả về phản hồi với mã trạng thái CONFLICT
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        // Tạo phản hồi thành công
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Xử lý yêu cầu quên mật khẩu và gửi mã OTP đến email của người dùng.
     *
     * @param email địa chỉ email của người dùng
     * @return thông báo xác nhận rằng mã OTP đã được gửi
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<CommonResponse> forgotPassword(@RequestParam("email") String email) {
        if (userService.CheckEmailExists(email)) {
            int otp = otpService.generateOtp(email);
            emailService.sendSimpleEmail(email, "Your OTP Code", "Your OTP Code is: " + otp);
            return new ResponseEntity<>(new CommonResponse(false, "OTP send to your email"), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new CommonResponse(true, "Email does not exist in the system"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Xác thực mã OTP được gửi đến email của người dùng.
     *
     * @param email địa chỉ email của người dùng
     * @param otp mã OTP cần xác thực
     * @return thông báo xác nhận rằng mã OTP hợp lệ hoặc thông báo lỗi nếu mã OTP không hợp lệ hoặc đã hết hạn
     */
    @PostMapping("/validate-otp")
    public ResponseEntity<CommonResponse> validateOtp(@RequestParam("email") String email,
                                                      @RequestParam("otp") int otp) {
        if (otpService.validateOtp(email, otp)) {
            return new ResponseEntity<>(new CommonResponse(false, "OTP is valid. You can now reset your password"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new CommonResponse(true, "Invalid OTP or OTP expired"), HttpStatus.UNAUTHORIZED);
        }
    }
}
