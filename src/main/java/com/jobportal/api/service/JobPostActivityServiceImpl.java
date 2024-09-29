package com.jobportal.api.service;

import com.jobportal.api.dto.request.job.CreateJobPostActivityRequest;
import com.jobportal.api.mapper.JobPostActivityMapper;
import com.jobportal.api.model.job.JobPostActivity;
import com.jobportal.api.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostActivityServiceImpl implements JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;
    private final JobPostActivityMapper jobPostActivityMapper;

    @Autowired
    public JobPostActivityServiceImpl(JobPostActivityRepository jobPostActivityRepository, JobPostActivityMapper jobPostActivityMapper) {
        this.jobPostActivityRepository = jobPostActivityRepository;
        this.jobPostActivityMapper = jobPostActivityMapper;
    }

    @Override
    public List<JobPostActivity> getJobPostActivities() {
        return jobPostActivityRepository.findAll();
    }

    @Override
    public Page<JobPostActivity> getListJobPostActivities(Pageable pageable) {
        return jobPostActivityRepository.findAll(pageable);
    }

    @Override
    public JobPostActivity getJobPostActivityById(String id) {
        return jobPostActivityRepository.findById(id).orElse(null);
    }

    @Override
    public JobPostActivity createJobPostActivity(CreateJobPostActivityRequest createJobPostActivityRequest) {
        JobPostActivity jobPostActivity = jobPostActivityMapper.mapCreateJobPostActivityRequesttoJobPostActivity(createJobPostActivityRequest);

        return jobPostActivityRepository.save(jobPostActivity);
    }
}
