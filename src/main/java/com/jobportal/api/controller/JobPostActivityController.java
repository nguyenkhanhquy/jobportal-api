package com.jobportal.api.controller;

import com.jobportal.api.dto.request.job.CreateJobPostActivityRequest;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.model.job.JobPostActivity;
import com.jobportal.api.service.JobPostActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs1")
public class JobPostActivityController {

    private final JobPostActivityService jobPostActivityService;

    @Autowired
    public JobPostActivityController(JobPostActivityService jobPostActivityService) {
        this.jobPostActivityService = jobPostActivityService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<JobPostActivity>>> getListJobPostActivities(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostActivityService.getJobPostActivities(request));
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

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<List<JobPostActivity>>> getListJobPostActivitiesByTitle(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostActivityService.getJobPostActivities(request));
    }

    @GetMapping("/filter")
    public ResponseEntity<SuccessResponse<List<JobPostActivity>>> getListJobPostActivitiesByTitleAndAddress(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostActivityService.getListJobPostActivitiesByTitleAndAddress(request));
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
