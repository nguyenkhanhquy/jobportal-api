package com.jobportal.api.service;

import com.jobportal.api.dto.request.profile.UpdateInfoJobSeekerRequest;
import com.jobportal.api.model.profile.JobSeekerProfile;
import org.springframework.web.multipart.MultipartFile;

public interface JobSeekerProfileService {

    JobSeekerProfile updateAvatar(MultipartFile multipart);

    JobSeekerProfile updateProfile(UpdateInfoJobSeekerRequest updateInfoJobSeekerRequest);
}
