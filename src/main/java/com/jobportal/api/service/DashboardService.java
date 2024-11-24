package com.jobportal.api.service;

import com.jobportal.api.dto.admin.OverviewDTO;
import org.springframework.security.access.prepost.PreAuthorize;

public interface DashboardService {

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    OverviewDTO getOverview();
}
