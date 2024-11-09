package com.jobportal.api.service;

import com.jobportal.api.dto.request.job.CreateJobPostActivityRequest;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.model.job.JobPostActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobPostActivityService {

    SuccessResponse<List<JobPostActivity>> getJobPostActivities(JobPostSearchFilterRequest request);

    Page<JobPostActivity> getListJobPostActivities(Pageable pageable);

    JobPostActivity getJobPostActivityById(String id);

    SuccessResponse<List<JobPostActivity>> getListJobPostActivitiesByTitleAndAddress(JobPostSearchFilterRequest request);

    JobPostActivity createJobPostActivity(CreateJobPostActivityRequest createJobPostActivityRequest);
}
