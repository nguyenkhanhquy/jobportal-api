package com.jobportal.api.service;

import com.jobportal.api.dto.request.job.CreateJobPostActivityRequest;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.mapper.JobPostActivityMapper;
import com.jobportal.api.model.job.JobPostActivity;
import com.jobportal.api.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public SuccessResponse<List<JobPostActivity>> getJobPostActivities(JobPostSearchFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<JobPostActivity> pageData;
        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            pageData = jobPostActivityRepository.findByTitleContainingIgnoreCase(request.getQuery(), pageable);
        } else {
            pageData = jobPostActivityRepository.findAll(pageable);
        }

        return SuccessResponse.<List<JobPostActivity>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent())
                .build();
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
    public Page<JobPostActivity> getListJobPostActivitiesByTitleAndAddress(String tile, String address, Pageable pageable) {
        return jobPostActivityRepository.findByTitleAndAddress(tile, address, pageable);
    }

    @Override
    public JobPostActivity createJobPostActivity(CreateJobPostActivityRequest createJobPostActivityRequest) {
        JobPostActivity jobPostActivity = jobPostActivityMapper.mapCreateJobPostActivityRequesttoJobPostActivity(createJobPostActivityRequest);

        return jobPostActivityRepository.save(jobPostActivity);
    }
}
