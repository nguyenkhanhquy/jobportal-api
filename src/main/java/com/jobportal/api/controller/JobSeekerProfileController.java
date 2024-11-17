package com.jobportal.api.controller;

import com.jobportal.api.dto.request.profile.UpdateInfoJobSeekerRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.model.profile.JobSeekerProfile;
import com.jobportal.api.service.JobSeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/job-seeker")
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;

    @Autowired
    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService) {
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @PostMapping("/update-avatar")
    public ResponseEntity<SuccessResponse<JobSeekerProfile>> updateAvatar(@RequestParam("avatar") MultipartFile multipart) {
        JobSeekerProfile jobSeekerProfile = jobSeekerProfileService.updateAvatar(multipart);

        SuccessResponse<JobSeekerProfile> successResponse = SuccessResponse.<JobSeekerProfile>builder()
                .message("Your file has been uploaded successfully")
                .result(jobSeekerProfile)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/update-profile")
    public ResponseEntity<SuccessResponse<Void>> updateProfile(@RequestBody UpdateInfoJobSeekerRequest updateInfoJobSeekerRequest) {
        jobSeekerProfileService.updateProfile(updateInfoJobSeekerRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Profile updated successfully")
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
