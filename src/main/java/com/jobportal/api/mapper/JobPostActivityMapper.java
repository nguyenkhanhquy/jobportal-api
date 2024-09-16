package com.jobportal.api.mapper;

import com.jobportal.api.dto.job.JobPostActivityDTO;
import com.jobportal.api.dto.request.job.CreateJobPostActivityRequest;
import com.jobportal.api.model.job.JobPostActivity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobPostActivityMapper {

    JobPostActivityDTO mapJobPostActivitytoJobPostActivityDTO(JobPostActivity jobPostActivity);

    JobPostActivity mapCreateJobPostActivityRequesttoJobPostActivity(CreateJobPostActivityRequest createJobPostActivityRequest);
}
