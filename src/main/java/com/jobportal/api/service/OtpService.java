package com.jobportal.api.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OtpService {

    private final Map<String, Integer> otpData = new HashMap<>();

    // Tạo OTP ngẫu nhiên
    public int generateOtp(String email) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpData.put(email, otp);

        // Đặt thời gian hết hạn OTP là 5 phút
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                otpData.remove(email);
            }
        }, 300000);

        return otp;
    }

    // Xác thực OTP
    public boolean validateOtp(String email, int otp) {
        if (otpData.containsKey(email) && otpData.get(email) == otp) {
            otpData.remove(email);
            return true;
        }
        return false;
    }
}
