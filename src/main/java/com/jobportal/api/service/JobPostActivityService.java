package com.jobportal.api.service;

import com.jobportal.api.dto.request.job.CreateJobPostActivityRequest;
import com.jobportal.api.model.job.JobPostActivity;

import java.util.List;

public interface JobPostActivityService {

    List<JobPostActivity> getJobPostActivities();

    JobPostActivity getJobPostActivityById(String id);

    JobPostActivity createJobPostActivity(CreateJobPostActivityRequest createJobPostActivityRequest);
}
