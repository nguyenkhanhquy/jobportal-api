package com.jobportal.api.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpServiceImpl implements OtpService {

    private final Map<String, Integer> otpData = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    // Tạo OTP ngẫu nhiên
    @Override
    public int generateOtp(String email) {
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
    @Override
    public boolean validateOtp(String email, int otp) {
        if (otpData.containsKey(email) && otpData.get(email) == otp) {
            otpData.remove(email);
            return true;
        }
        return false;
    }
}
