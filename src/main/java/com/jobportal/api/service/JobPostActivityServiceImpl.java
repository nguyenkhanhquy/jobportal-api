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
    public JobPostActivity getJobPostActivityById(String id) {
        return jobPostActivityRepository.findById(id).orElse(null);
    }

    @Override
    public SuccessResponse<List<JobPostActivity>> getListJobPostActivitiesByTitleAndAddress(JobPostSearchFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<JobPostActivity> pageData;
        if (request.getQuery() != null && !request.getQuery().isBlank() && request.getAddress() != null && !request.getAddress().isBlank()) {
            pageData = jobPostActivityRepository.findByTitleContainingIgnoreCaseAndAddress(request.getQuery(), request.getAddress(), pageable);
        } else if (request.getQuery() != null && !request.getQuery().isBlank()) {
            pageData = jobPostActivityRepository.findByTitleContainingIgnoreCase(request.getQuery(), pageable);
        } else if (request.getAddress() != null && !request.getAddress().isBlank()) {
            pageData = jobPostActivityRepository.findByAddressContainingIgnoreCase(request.getAddress(), pageable);
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
    public JobPostActivity createJobPostActivity(CreateJobPostActivityRequest createJobPostActivityRequest) {
        JobPostActivity jobPostActivity = jobPostActivityMapper.mapCreateJobPostActivityRequesttoJobPostActivity(createJobPostActivityRequest);

        return jobPostActivityRepository.save(jobPostActivity);
    }
}
