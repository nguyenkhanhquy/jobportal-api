package com.jobportal.api.controller;

import com.jobportal.api.dto.admin.OverviewDTO;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public ResponseEntity<SuccessResponse<OverviewDTO>> getOverview() {
        OverviewDTO overviewDTO = dashboardService.getOverview();

        SuccessResponse<OverviewDTO> successResponse = SuccessResponse.<OverviewDTO>builder()
                .result(overviewDTO)
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
