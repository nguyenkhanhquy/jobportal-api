package com.jobportal.api.dto.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostActivityDTO {

    private String id;

    private String logo;

    private String title;

    private String company;

    private String salary;

    private String address;

    private String experience;
}
