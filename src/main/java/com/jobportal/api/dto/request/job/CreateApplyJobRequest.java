package com.jobportal.api.dto.request.job;

import lombok.Getter;

@Getter
public class CreateApplyJobRequest {

    private String jobPostId;

    private String coverLetter;

    private String cv;
}
