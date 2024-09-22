package com.jobportal.api.dto.request.profile;

import lombok.Getter;

@Getter
public class UpdateInfoJobSeekerRequest {

    private String fullName;

    private String address;

    private String workExperience;
}
