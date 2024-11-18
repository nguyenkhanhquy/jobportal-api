package com.jobportal.api.dto.request.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
public class UpdateInfoJobSeekerRequest {

    private String fullName;

    private String address;

    private String workExperience;

    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dob;
}
