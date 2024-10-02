package com.jobportal.api.service;

import com.jobportal.api.dto.request.job.CreateJobPostActivityRequest;
import com.jobportal.api.model.job.JobPostActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobPostActivityService {

    List<JobPostActivity> getJobPostActivities();

    Page<JobPostActivity> getListJobPostActivities(Pageable pageable);

    JobPostActivity getJobPostActivityById(String id);

    Page<JobPostActivity> getListJobPostActivitiesByTitle(String tile, Pageable pageable);

    JobPostActivity createJobPostActivity(CreateJobPostActivityRequest createJobPostActivityRequest);
}
