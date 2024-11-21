package com.jobportal.api.service;

import com.jobportal.api.dto.job.jobapply.JobApplyDTO;
import com.jobportal.api.dto.job.jobapply.JobApplyDetailDTO;
import com.jobportal.api.dto.request.job.CreateApplyJobRequest;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobApplyService {

    @PreAuthorize("hasAuthority('SCOPE_JOB_SEEKER')")
    void createApplyJob(CreateApplyJobRequest request);

    String uploadCV(MultipartFile multipart);

    @PreAuthorize("hasAuthority('SCOPE_JOB_SEEKER')")
    SuccessResponse<List<JobApplyDTO>> getJobApplyByJobSeekerProfile(JobPostSearchFilterRequest request);

    List<JobApplyDetailDTO> getALlJobApplyByJobPostId(String id);
}
