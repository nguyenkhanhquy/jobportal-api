package com.jobportal.api.service;

import com.jobportal.api.dto.request.job.CreateApplyJobRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface JobApplyService {

    @PreAuthorize("hasAuthority('SCOPE_JOB_SEEKER')")
    void createApplyJob(CreateApplyJobRequest request);

    String uploadCV(MultipartFile multipart);
}
