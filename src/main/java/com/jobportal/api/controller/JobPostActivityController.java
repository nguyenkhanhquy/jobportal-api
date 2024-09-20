package com.jobportal.api.controller;

import com.jobportal.api.dto.request.job.CreateJobPostActivityRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.model.job.JobPostActivity;
import com.jobportal.api.service.JobPostActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobPostActivityController {

    private final JobPostActivityService jobPostActivityService;

    @Autowired
    public JobPostActivityController(JobPostActivityService jobPostActivityService) {
        this.jobPostActivityService = jobPostActivityService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<JobPostActivity>>> getAllJobPostActivities() {
        List<JobPostActivity> jobPostActivities = jobPostActivityService.getJobPostActivities();

        SuccessResponse<List<JobPostActivity>> successResponse = SuccessResponse.<List<JobPostActivity>>builder()
                .message("getJobPostActivities")
                .result(jobPostActivities)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<JobPostActivity>> getJobPostActivityById(@PathVariable("id") String id) {
        JobPostActivity jobPostActivity = jobPostActivityService.getJobPostActivityById(id);

        SuccessResponse<JobPostActivity> successResponse = SuccessResponse.<JobPostActivity>builder()
                .message("getJobPostActivityById")
                .result(jobPostActivity)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<JobPostActivity>> createJobPostActivity(@RequestBody CreateJobPostActivityRequest createJobPostActivityRequest) {
        JobPostActivity jobPostActivity = jobPostActivityService.createJobPostActivity(createJobPostActivityRequest);

        SuccessResponse<JobPostActivity> successResponse = SuccessResponse.<JobPostActivity>builder()
                .message("createJobPostActivity")
                .result(jobPostActivity)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
