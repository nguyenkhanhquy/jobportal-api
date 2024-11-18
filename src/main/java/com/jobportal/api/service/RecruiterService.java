package com.jobportal.api.service;

import com.jobportal.api.dto.business.RecruiterDTO;
import com.jobportal.api.dto.request.profile.UpdateRecruiterProfileRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RecruiterService {

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    List<RecruiterDTO> getAllRecruiters();

    @PostAuthorize("returnObject.userId == authentication.principal.claims['userId'] or hasAuthority('SCOPE_ADMIN')")
    RecruiterDTO getRecruiterById(String id);

    void updateRecruiterProfile(UpdateRecruiterProfileRequest request);
}
