package com.jobportal.api.service;

import com.jobportal.api.dto.job.jobpost.JobPostBasicDTO;
import com.jobportal.api.dto.job.jobsaved.JobSavedDTO;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JobSavedService {

    @PreAuthorize("hasAuthority('SCOPE_JOB_SEEKER')")
    SuccessResponse<List<JobSavedDTO>> getAllSavedJobPosts(JobPostSearchFilterRequest request);

    @PreAuthorize("hasAuthority('SCOPE_JOB_SEEKER')")
    @Transactional
    void deleteAllSavedJobPosts();
}
