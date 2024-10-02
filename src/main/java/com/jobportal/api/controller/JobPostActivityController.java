package com.jobportal.api.controller;

import com.jobportal.api.dto.page.PageResponse;
import com.jobportal.api.dto.request.job.CreateJobPostActivityRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.model.job.JobPostActivity;
import com.jobportal.api.service.JobPostActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobPostActivityController {

    private final JobPostActivityService jobPostActivityService;

    @Autowired
    public JobPostActivityController(JobPostActivityService jobPostActivityService) {
        this.jobPostActivityService = jobPostActivityService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<JobPostActivity>> getListJobPostActivities(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                                  @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<JobPostActivity> jobPostActivities = jobPostActivityService.getListJobPostActivities(pageable);

        PageResponse.PageInfo pageInfo = PageResponse.PageInfo.builder()
                .pageNumber(jobPostActivities.getNumber() + 1)
                .size(jobPostActivities.getSize())
                .totalElements(jobPostActivities.getTotalElements())
                .totalPages(jobPostActivities.getTotalPages())
                .build();

        PageResponse<JobPostActivity> pageResponse = PageResponse.<JobPostActivity>builder()
                .result(jobPostActivities.getContent())
                .page(pageInfo)
                .build();

        return ResponseEntity.ok(pageResponse);
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
    public ResponseEntity<PageResponse<JobPostActivity>> getListJobPostActivitiesByTitle(@RequestParam(value = "query", required = false) String query,
                                                                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                         @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<JobPostActivity> jobPostActivities;
        if (query == null || query.isEmpty()) {
            jobPostActivities = jobPostActivityService.getListJobPostActivities(pageable);
        } else {
            jobPostActivities = jobPostActivityService.getListJobPostActivitiesByTitle(query, pageable);
        }

        PageResponse.PageInfo pageInfo = PageResponse.PageInfo.builder()
                .pageNumber(jobPostActivities.getNumber() + 1)
                .size(jobPostActivities.getSize())
                .totalElements(jobPostActivities.getTotalElements())
                .totalPages(jobPostActivities.getTotalPages())
                .build();

        PageResponse<JobPostActivity> pageResponse = PageResponse.<JobPostActivity>builder()
                .result(jobPostActivities.getContent())
                .page(pageInfo)
                .build();

        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/filter")
    public ResponseEntity<PageResponse<JobPostActivity>> getListJobPostActivitiesByTitle(@RequestParam(value = "title", required = false) String tile,
                                                                                         @RequestParam(value = "address", required = false) String address,
                                                                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                         @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<JobPostActivity> jobPostActivities = jobPostActivityService.getListJobPostActivitiesByTitleAndAddress(tile, address, pageable);

        PageResponse.PageInfo pageInfo = PageResponse.PageInfo.builder()
                .pageNumber(jobPostActivities.getNumber() + 1)
                .size(jobPostActivities.getSize())
                .totalElements(jobPostActivities.getTotalElements())
                .totalPages(jobPostActivities.getTotalPages())
                .build();

        PageResponse<JobPostActivity> pageResponse = PageResponse.<JobPostActivity>builder()
                .result(jobPostActivities.getContent())
                .page(pageInfo)
                .build();

        return ResponseEntity.ok(pageResponse);
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
