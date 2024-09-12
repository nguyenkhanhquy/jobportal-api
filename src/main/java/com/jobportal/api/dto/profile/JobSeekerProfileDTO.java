package com.jobportal.api.dto.profile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JobSeekerProfileDTO {

    private String email;

    private boolean isActive;

    private String fullName;

    private String address;

    private String workExperience;
}
