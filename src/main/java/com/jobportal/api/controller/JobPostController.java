package com.jobportal.api.controller;

import com.jobportal.api.dto.job.jobpost.JobPostDTO;
import com.jobportal.api.dto.job.jobpost.JobPostDetailDTO;
import com.jobportal.api.dto.request.job.CreateJobPostRequest;
import com.jobportal.api.dto.request.job.JobPostHiddenRequest;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.request.job.JobPostUpdateRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.service.JobPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobPostController {

    private final JobPostService jobPostService;

    @Autowired
    public JobPostController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @GetMapping("/admin")
    public ResponseEntity<SuccessResponse<List<JobPostDetailDTO>>> getAllJobPostsAdmin(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostService.getAllJobPostsAdmin(request));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<JobPostDetailDTO>>> getAllJobPosts(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostService.getAllJobPosts(request));
    }

    @GetMapping("/popular")
    public ResponseEntity<SuccessResponse<List<JobPostDetailDTO>>> getPopularJobPosts(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostService.getPopularJobPosts(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<JobPostDetailDTO>> getJobPostById(@PathVariable("id") String id) {
        JobPostDetailDTO jobPostDetailDTO = jobPostService.getJobPostById(id);

        SuccessResponse<JobPostDetailDTO> successResponse = SuccessResponse.<JobPostDetailDTO>builder()
                .result(jobPostDetailDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createJobPost(@Valid @RequestBody CreateJobPostRequest createJobPostRequest) {
        jobPostService.createJobPost(createJobPostRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Tạo bài đăng việc làm thành công")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPost(@PathVariable("id") String id) {
        jobPostService.deleteJobPost(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    public ResponseEntity<SuccessResponse<Void>> saveJobPost(@RequestBody Map<String, String> request) {
        String message = jobPostService.saveJobPost(request)
                ? "Lưu công việc thành công"
                : "Bỏ lưu công việc thành công";

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message(message)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/recruiter")
    public ResponseEntity<SuccessResponse<List<JobPostDTO>>> getJobPostByRecruiter(@ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostService.getJobPostByRecruiter(request));
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<SuccessResponse<List<JobPostDTO>>> getJobPostByCompanyId(@PathVariable("id") String id, @ModelAttribute JobPostSearchFilterRequest request) {
        return ResponseEntity.ok(jobPostService.getJobPostByCompanyId(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> updateJobPost(@PathVariable("id") String id, @Valid @RequestBody JobPostUpdateRequest jobPostUpdateRequest) {
        jobPostService.updateJobPost(id, jobPostUpdateRequest);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Cập nhật bài đăng việc làm thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/hidden")
    public ResponseEntity<SuccessResponse<Void>> hiddenJobPost(@RequestBody JobPostHiddenRequest request) {
        String message = jobPostService.hiddenJobPost(request.getId())
                ? "Ẩn bài đăng thành công"
                : "Hiện bài đăng thành công";

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message(message)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
