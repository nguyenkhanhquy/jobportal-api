package com.jobportal.api.service;

public interface EmailService {

    void sendSimpleEmail(String to, String subject, String text);
}
