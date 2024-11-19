package com.jobportal.api.controller;

import com.jobportal.api.dto.job.jobapply.JobApplyDTO;
import com.jobportal.api.dto.request.job.CreateApplyJobRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.model.job.JobApply;
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
    public ResponseEntity<SuccessResponse<List<JobApplyDTO>>> getJobApplyByJobSeekerProfile() {

        List<JobApplyDTO> jobApplyDTOs = jobApplyService.getJobApplyByJobSeekerProfile();

        SuccessResponse<List<JobApplyDTO>> successResponse = SuccessResponse.<List<JobApplyDTO>>builder()
                .message("Lấy danh sách ứng tuyển thành công")
                .result(jobApplyDTOs)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
