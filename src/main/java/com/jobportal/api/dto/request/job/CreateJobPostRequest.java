package com.jobportal.api.dto.request.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Date;

@Getter
public class CreateJobPostRequest {

    private String title;

    private String type;

    private String remote;

    private String description;

    private String salary;

    private Integer quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date expiryDate;

    private String jobPosition;

    private String requirements;

    private String benefits;

    private String address;
}
