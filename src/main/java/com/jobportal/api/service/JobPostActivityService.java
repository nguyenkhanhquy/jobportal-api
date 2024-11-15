package com.jobportal.api.service;

import com.jobportal.api.dto.request.job.CreateJobPostActivityRequest;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.model.job.JobPostActivity;

import java.util.List;

public interface JobPostActivityService {

    SuccessResponse<List<JobPostActivity>> getJobPostActivities(JobPostSearchFilterRequest request);

    JobPostActivity getJobPostActivityById(String id);

    SuccessResponse<List<JobPostActivity>> getListJobPostActivitiesByTitleAndAddress(JobPostSearchFilterRequest request);

    JobPostActivity createJobPostActivity(CreateJobPostActivityRequest createJobPostActivityRequest);
}
