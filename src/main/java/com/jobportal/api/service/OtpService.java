package com.jobportal.api.service;

public interface OtpService {

    int generateOtp(String email);
    boolean validateOtp(String email, int otp);
}
