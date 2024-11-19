package com.jobportal.api.controller;

import com.jobportal.api.dto.job.jobsaved.JobSavedDTO;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.service.JobSavedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs/saved")
public class JobSavedController {

    private final JobSavedService jobSavedService;

    @Autowired
    public JobSavedController(JobSavedService jobSavedService) {
        this.jobSavedService = jobSavedService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<JobSavedDTO>>> getAllSavedJobPosts(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobSavedService.getAllSavedJobPosts(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllSavedJobPosts() {
        jobSavedService.deleteAllSavedJobPosts();

        return ResponseEntity.noContent().build();
    }
}
