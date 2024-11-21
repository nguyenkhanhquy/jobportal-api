package com.jobportal.api.dto.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jobportal.api.model.profile.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterProfileDTO {

    private String id;

    private String email;

    private String name;

    private boolean isActive;

    private boolean isLocked;

    private Company company;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date registrationDate;

    private String position;

    private String phone;

    private String recruiterEmail;
}
