package com.jobportal.api.controller;

import com.jobportal.api.dto.business.RecruiterDTO;
import com.jobportal.api.dto.request.profile.UpdateRecruiterProfileRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.service.RecruiterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruiters")
public class RecruiterController {

    private final RecruiterService recruiterService;

    @Autowired
    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<RecruiterDTO>>> getAllRecruiters() {
        List<RecruiterDTO> recruiterDTOS = recruiterService.getAllRecruiters();

        SuccessResponse<List<RecruiterDTO>> successResponse = SuccessResponse.<List<RecruiterDTO>>builder()
                .result(recruiterDTOS)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<RecruiterDTO>> getRecruiterById(@PathVariable("id") String id) {
        RecruiterDTO recruiterDTO = recruiterService.getRecruiterById(id);

        SuccessResponse<RecruiterDTO> successResponse = SuccessResponse.<RecruiterDTO>builder()
                .result(recruiterDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/update-profile")
    public ResponseEntity<SuccessResponse<Void>> updateRecruiterProfile(@Valid @RequestBody UpdateRecruiterProfileRequest request) {
        recruiterService.updateRecruiterProfile(request);

        SuccessResponse<Void> successResponse = SuccessResponse.<Void>builder()
                .message("Cập nhật hồ sơ thành công")
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
