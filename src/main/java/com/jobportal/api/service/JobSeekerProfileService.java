package com.jobportal.api.service;

import com.jobportal.api.model.profile.JobSeekerProfile;
import org.springframework.web.multipart.MultipartFile;

public interface JobSeekerProfileService {

    JobSeekerProfile updateAvatar(MultipartFile multipart);
}
