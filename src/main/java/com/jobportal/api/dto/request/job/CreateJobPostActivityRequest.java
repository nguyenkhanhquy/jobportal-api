package com.jobportal.api.dto.request.job;

import lombok.Getter;

@Getter
public class CreateJobPostActivityRequest {

    private String id;

    private String logo;

    private String title;

    private String company;

    private String salary;

    private String address;

    private String experience;

    private String type;

    private String applicants;

    private String gender;

    private String deadline;

    private String description;

    private String requirements;

    private String benefits;
}
