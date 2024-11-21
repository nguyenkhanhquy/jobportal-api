package com.jobportal.api.service;

import com.jobportal.api.dto.profile.JobSeekerProfileDTO;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.request.profile.UpdateInfoJobSeekerRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.model.profile.JobSeekerProfile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobSeekerProfileService {

    JobSeekerProfile updateAvatar(MultipartFile multipart);

    void updateProfile(UpdateInfoJobSeekerRequest updateInfoJobSeekerRequest);

    SuccessResponse<List<JobSeekerProfileDTO>> getAllJobSeekers(JobPostSearchFilterRequest request);
}
