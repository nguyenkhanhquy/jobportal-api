package com.jobportal.api.service;

import com.jobportal.api.dto.job.jobpost.JobPostDTO;
import com.jobportal.api.dto.job.jobpost.JobPostDetailDTO;
import com.jobportal.api.dto.request.job.CreateJobPostRequest;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.request.job.JobPostUpdateRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;

public interface JobPostService {

    SuccessResponse<List<JobPostDetailDTO>> getAllJobPostsAdmin(JobPostSearchFilterRequest request);

    SuccessResponse<List<JobPostDetailDTO>> getAllJobPosts(JobPostSearchFilterRequest request);

    SuccessResponse<List<JobPostDetailDTO>> getPopularJobPosts(JobPostSearchFilterRequest request);

    JobPostDetailDTO getJobPostById(String id);

    @PreAuthorize("hasAuthority('SCOPE_RECRUITER')")
    void createJobPost(CreateJobPostRequest createJobPostRequest);

    @PostAuthorize("hasAuthority('SCOPE_RECRUITER')")
    void deleteJobPost(String id);

    @PreAuthorize("hasAuthority('SCOPE_JOB_SEEKER')")
    boolean saveJobPost(Map<String, String> request);

    SuccessResponse<List<JobPostDTO>> getJobPostByRecruiter(JobPostSearchFilterRequest request);

    SuccessResponse<List<JobPostDTO>> getJobPostByCompanyId(String id, JobPostSearchFilterRequest request);

    @PostAuthorize("hasAuthority('SCOPE_RECRUITER')")
    void updateJobPost(String id, JobPostUpdateRequest jobPostUpdateRequest);

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    boolean hiddenJobPost(String id);
}
