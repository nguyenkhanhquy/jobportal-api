package com.jobportal.api.service;

import com.jobportal.api.dto.job.jobpost.JobPostBasicDTO;
import com.jobportal.api.dto.job.jobpost.JobPostDetailDTO;
import com.jobportal.api.dto.request.job.CreateJobPostRequest;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.JobPostMapper;
import com.jobportal.api.model.job.JobPost;
import com.jobportal.api.model.profile.RecruiterProfile;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.JobPostRepository;
import com.jobportal.api.repository.RecruiterProfileRepository;
import com.jobportal.api.repository.UserRepository;
import com.jobportal.api.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JobPostServiceImpl implements JobPostService {

    private final JobPostRepository jobPostRepository;
    private final RecruiterProfileRepository recruiterRepository;
    private final UserRepository userRepository;
//    private final StudentRepository studentRepository;
//    private final JobSavedRepository jobSavedRepository;
    private final JobPostMapper jobPostMapper;

    @Autowired
    public JobPostServiceImpl(JobPostRepository jobPostRepository, RecruiterProfileRepository recruiterRepository, UserRepository userRepository, JobPostMapper jobPostMapper) {
        this.jobPostRepository = jobPostRepository;
        this.recruiterRepository = recruiterRepository;
        this.userRepository = userRepository;
        this.jobPostMapper = jobPostMapper;
    }

    @Override
    public SuccessResponse<List<JobPostDetailDTO>> getAllJobPosts(JobPostSearchFilterRequest request) {
        return null;
    }

    @Override
    public SuccessResponse<List<JobPostBasicDTO>> getPopularJobPosts(JobPostSearchFilterRequest request) {
        return null;
    }

    @Override
    public JobPostDetailDTO getJobPostById(String id) {
        return null;
    }

    @Override
    public void createJobPost(CreateJobPostRequest createJobPostRequest) {
        User user = AuthUtil.getAuthenticatedUser(userRepository);

        RecruiterProfile recruiter = recruiterRepository.findByUser(user);
        if (recruiter == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }

        JobPost jobPost = JobPost.builder()
                .title(createJobPostRequest.getTitle())
                .type(createJobPostRequest.getType())
                .remote(createJobPostRequest.getRemote())
                .description(createJobPostRequest.getDescription())
                .salary(createJobPostRequest.getSalary())
                .quantity(createJobPostRequest.getQuantity())
                .createdDate(Date.from(Instant.now()))
                .updatedDate(Date.from(Instant.now()))
                .expiryDate(createJobPostRequest.getExpiryDate())
                .jobPosition(createJobPostRequest.getJobPosition())
                .requirements(createJobPostRequest.getRequirements())
                .benefits(createJobPostRequest.getBenefits())
                .company(recruiter.getCompany())
                .address(createJobPostRequest.getAddress())
                .build();

        jobPostRepository.save(jobPost);
    }

    @Override
    public void deleteJobPost(String id) {

    }

    @Override
    public boolean saveJobPost(Map<String, String> request) {
        return false;
    }
}
