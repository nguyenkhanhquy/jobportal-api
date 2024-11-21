package com.jobportal.api.controller;

import com.jobportal.api.dto.job.jobapply.JobApplyDTO;
import com.jobportal.api.dto.job.jobapply.JobApplyDetailDTO;
import com.jobportal.api.dto.request.job.CreateApplyJobRequest;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.service.JobApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/jobs/apply")
public class JobApplyController {

    private final JobApplyService jobApplyService;

    @Autowired
    public JobApplyController(JobApplyService jobApplyService) {
        this.jobApplyService = jobApplyService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createJobApply(@RequestBody CreateApplyJobRequest createApplyJobRequest) {
        jobApplyService.createApplyJob(createApplyJobRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Ứng tuyển thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/upload-cv")
    public ResponseEntity<SuccessResponse<String>> uploadCV(@RequestParam("cv") MultipartFile multipart) {
        String cvUrl = jobApplyService.uploadCV(multipart);

        SuccessResponse<String> successResponse = SuccessResponse.<String>builder()
                .message("Tải lên cv thành công")
                .result(cvUrl)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<JobApplyDTO>>> getJobApplyByJobSeekerProfile(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobApplyService.getJobApplyByJobSeekerProfile(request));
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<SuccessResponse<List<JobApplyDetailDTO>>> getJobApplyById(@PathVariable String id) {
        SuccessResponse<List<JobApplyDetailDTO>> successResponse = SuccessResponse.<List<JobApplyDetailDTO>>builder()
                .result(jobApplyService.getALlJobApplyByJobPostId(id))
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
