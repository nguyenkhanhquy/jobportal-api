package com.jobportal.api.dto.request.profile;

import lombok.Getter;

@Getter
public class UpdateRecruiterProfileRequest {

    private String name;

    private String position;

    private String phone;

    private String recruiterEmail;

    private String website;

    private String description;

    private String companyAddress;

    private String companyLogo;
}
