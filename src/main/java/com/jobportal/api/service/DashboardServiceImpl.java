package com.jobportal.api.service;

import com.jobportal.api.dto.admin.OverviewDTO;
import com.jobportal.api.repository.JobApplyRepository;
import com.jobportal.api.repository.JobPostRepository;
import com.jobportal.api.repository.JobSeekerProfileRepository;
import com.jobportal.api.repository.RecruiterProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobPostRepository jobPostRepository;
    private final JobApplyRepository jobApplyRepository;

    @Override
    public OverviewDTO getOverview() {
        return OverviewDTO.builder()
                .totalJobSeekers(jobSeekerProfileRepository.count())
                .totalRecruiters(recruiterProfileRepository.count())
                .totalJobPosts(jobPostRepository.count())
                .totalJobApplies(jobApplyRepository.count())
                .build();
    }
}
