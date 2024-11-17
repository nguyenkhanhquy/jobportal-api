package com.jobportal.api.dto.request.auth;

import lombok.Getter;

@Getter
public class RegisterRecruiterRequest {

    private String email;

    private String password;

    private String company;

    private String name;

    private String position;

    private String phone;

    private String recruiterEmail;
}
