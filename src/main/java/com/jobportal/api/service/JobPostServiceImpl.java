package com.jobportal.api.service;

import com.jobportal.api.dto.job.jobpost.JobPostBasicDTO;
import com.jobportal.api.dto.job.jobpost.JobPostDTO;
import com.jobportal.api.dto.job.jobpost.JobPostDetailDTO;
import com.jobportal.api.dto.request.job.CreateJobPostRequest;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.request.job.JobPostUpdateRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.JobPostMapper;
import com.jobportal.api.model.job.JobPost;
import com.jobportal.api.model.job.JobSaved;
import com.jobportal.api.model.profile.JobSeekerProfile;
import com.jobportal.api.model.profile.RecruiterProfile;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.*;
import com.jobportal.api.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {

    private final JobPostRepository jobPostRepository;
    private final RecruiterProfileRepository recruiterRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UserRepository userRepository;
    private final JobSavedRepository jobSavedRepository;
    private final JobPostMapper jobPostMapper;

    @Override
    public SuccessResponse<List<JobPostDetailDTO>> getAllJobPostsAdmin(JobPostSearchFilterRequest request) {
        Sort sort;
        if ("oldest".equalsIgnoreCase(request.getOrder())) {
            sort = Sort.by(Sort.Order.asc("createdDate"));
        } else if ("recentUpdate".equalsIgnoreCase(request.getOrder())) {
            sort = Sort.by(Sort.Order.desc("updatedDate"));
        } else {
            sort = Sort.by(Sort.Order.desc("createdDate"));
        }
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<JobPost> pageData;
        if (request.getQuery() != null && !request.getQuery().isBlank()) {
//            pageData = jobPostRepository.findByTitleContainingIgnoreCase(request.getQuery(), pageable);
            pageData = jobPostRepository.searchByKeywordAdmin(request.getQuery(), pageable);
        } else {
            pageData = jobPostRepository.findAll(pageable);
        }

        return SuccessResponse.<List<JobPostDetailDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent().stream()
                        .map(jobPostMapper::mapJobPostToJobPostDetailDTO)
                        .toList())
                .build();

    }

    @Override
    public SuccessResponse<List<JobPostDetailDTO>> getAllJobPosts(JobPostSearchFilterRequest request) {
        Sort sort;
        if ("oldest".equalsIgnoreCase(request.getOrder())) {
            sort = Sort.by(Sort.Order.asc("createdDate"));
        } else if ("recentUpdate".equalsIgnoreCase(request.getOrder())) {
            sort = Sort.by(Sort.Order.desc("updatedDate"));
        } else {
            sort = Sort.by(Sort.Order.desc("createdDate"));
        }
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<JobPost> pageData;
        if (request.getQuery() != null && !request.getQuery().isBlank()) {
//            pageData = jobPostRepository.findByTitleContainingIgnoreCase(request.getQuery(), pageable);
            pageData = jobPostRepository.searchByKeyword(request.getQuery(), pageable);
        } else {
            pageData = jobPostRepository.findAllByisHidden(false, pageable);
        }

        try {
            User user = AuthUtil.getAuthenticatedUser(userRepository);
            JobSeekerProfile jobSeeker = jobSeekerProfileRepository.findByUser(user);
            if (jobSeeker == null) {
                throw new CustomException(EnumException.PROFILE_NOT_FOUND);
            }

            List<JobSaved> jobSavedList = jobSavedRepository.findByJobSeekerProfile(jobSeeker);

            // Tạo danh sách ID của các công việc đã lưu
            Set<String> savedJobPostIds = jobSavedList.stream()
                    .map(jobSaved -> jobSaved.getJobPost().getId())
                    .collect(Collectors.toSet());

            List<JobPostDetailDTO> jobPostDetails = pageData.getContent().stream()
                    .map(jobPost -> {
                        JobPostDetailDTO dto = jobPostMapper.mapJobPostToJobPostDetailDTO(jobPost);
                        // Kiểm tra nếu jobPostId nằm trong savedJobPostIds, cập nhật isSaved
                        if (savedJobPostIds.contains(jobPost.getId())) {
                            dto.setSaved(true);
                        }
                        return dto;
                    })
                    .toList();

            return SuccessResponse.<List<JobPostDetailDTO>>builder()
                    .pageInfo(SuccessResponse.PageInfo.builder()
                            .currentPage(request.getPage())
                            .totalPages(pageData.getTotalPages())
                            .pageSize(pageData.getSize())
                            .totalElements(pageData.getTotalElements())
                            .hasPreviousPage(pageData.hasPrevious())
                            .hasNextPage(pageData.hasNext())
                            .build())
                    .result(jobPostDetails)
                    .build();
        } catch (CustomException e) {
            return SuccessResponse.<List<JobPostDetailDTO>>builder()
                    .pageInfo(SuccessResponse.PageInfo.builder()
                            .currentPage(request.getPage())
                            .totalPages(pageData.getTotalPages())
                            .pageSize(pageData.getSize())
                            .totalElements(pageData.getTotalElements())
                            .hasPreviousPage(pageData.hasPrevious())
                            .hasNextPage(pageData.hasNext())
                            .build())
                    .result(pageData.getContent().stream()
                            .map(jobPostMapper::mapJobPostToJobPostDetailDTO)
                            .toList())
                    .build();
        }
    }

    @Override
    public SuccessResponse<List<JobPostBasicDTO>> getPopularJobPosts(JobPostSearchFilterRequest request) {
        Sort sort;
        if ("oldest".equalsIgnoreCase(request.getOrder())) {
            sort = Sort.by(Sort.Order.asc("createdDate"));
        } else {
            sort = Sort.by(Sort.Order.desc("createdDate"));
        }
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<JobPost> pageData;
        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            pageData = jobPostRepository.findByTitleContainingIgnoreCase(request.getQuery(), pageable);
        } else {
            pageData = jobPostRepository.findAllByisHidden(false, pageable);
        }

        return SuccessResponse.<List<JobPostBasicDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent().stream()
                        .map(jobPostMapper::mapJobPostToJobPostBasicDTO)
                        .toList())
                .build();
    }

    @Override
    public JobPostDetailDTO getJobPostById(String id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));

        JobPostDetailDTO jobPostDetailDTO = jobPostMapper.mapJobPostToJobPostDetailDTO(jobPost);

        try {
            User user = AuthUtil.getAuthenticatedUser(userRepository);

            JobSeekerProfile jobSeeker = jobSeekerProfileRepository.findByUser(user);
            if (jobSeeker == null) {
                throw new CustomException(EnumException.PROFILE_NOT_FOUND);
            }

            if (jobSavedRepository.existsByJobSeekerProfileAndJobPost(jobSeeker, jobPost)) {
                jobPostDetailDTO.setSaved(true);
            }
            return jobPostDetailDTO;
        } catch (CustomException e) {
            return jobPostDetailDTO;
        }
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
                .companyName(recruiter.getCompany().getName())
                .address(createJobPostRequest.getAddress())
                .build();

        jobPostRepository.save(jobPost);
    }

    @Override
    public void deleteJobPost(String id) {

    }

    @Override
    public boolean saveJobPost(Map<String, String> request) {
        User user = AuthUtil.getAuthenticatedUser(userRepository);

        JobSeekerProfile jobSeeker = jobSeekerProfileRepository.findByUser(user);
        if (jobSeeker == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }

        JobPost jobPost = jobPostRepository.findById(request.get("id"))
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));

        JobSaved jobSaved = jobSavedRepository.findByJobSeekerProfileAndJobPost(jobSeeker, jobPost);
        if (jobSaved != null) {
            jobSavedRepository.delete(jobSaved);
            return false;
        }

        jobSavedRepository.save(JobSaved.builder()
                .savedDate(Date.from(Instant.now()))
                .jobSeekerProfile(jobSeeker)
                .jobPost(jobPost)
                .build());

        return true;
    }

    @Override
    public SuccessResponse<List<JobPostDTO>> getJobPostByRecruiter(JobPostSearchFilterRequest request) {
        User user = AuthUtil.getAuthenticatedUser(userRepository);

        RecruiterProfile recruiter = recruiterRepository.findByUser(user);
        if (recruiter == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }

        Sort sort;
        if ("oldest".equalsIgnoreCase(request.getOrder())) {
            sort = Sort.by(Sort.Order.asc("updatedDate"));
        } else {
            sort = Sort.by(Sort.Order.desc("updatedDate"));
        }
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<JobPost> pageData;
        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            pageData = jobPostRepository.findByCompanyIdAndTitleContainingIgnoreCase(recruiter.getCompany().getId(), request.getQuery(), pageable);
        } else {
            pageData = jobPostRepository.findByCompanyId(recruiter.getCompany().getId(), pageable);
        }

        return SuccessResponse.<List<JobPostDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent().stream()
                        .map(jobPostMapper::mapJobPostToJobPostDTO)
                        .toList())
                .build();
    }

    @Override
    public SuccessResponse<List<JobPostDTO>> getJobPostByCompanyId(String id, JobPostSearchFilterRequest request) {
        Sort sort;
        if ("oldest".equalsIgnoreCase(request.getOrder())) {
            sort = Sort.by(Sort.Order.asc("updatedDate"));
        } else {
            sort = Sort.by(Sort.Order.desc("updatedDate"));
        }
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<JobPost> pageData;
        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            pageData = jobPostRepository.findByCompanyIdAndTitleContainingIgnoreCase(id, request.getQuery(), pageable);
        } else {
            pageData = jobPostRepository.findByCompanyId(id, pageable);
        }

        return SuccessResponse.<List<JobPostDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent().stream()
                        .map(jobPostMapper::mapJobPostToJobPostDTO)
                        .toList())
                .build();
    }

    @Override
    public void updateJobPost(String id, JobPostUpdateRequest jobPostUpdateRequest) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));

        jobPost.setTitle(jobPostUpdateRequest.getTitle());
        jobPost.setType(jobPostUpdateRequest.getType());
        jobPost.setRemote(jobPostUpdateRequest.getRemote());
        jobPost.setDescription(jobPostUpdateRequest.getDescription());
        jobPost.setSalary(jobPostUpdateRequest.getSalary());
        jobPost.setQuantity(jobPostUpdateRequest.getQuantity());
        jobPost.setUpdatedDate(Date.from(Instant.now()));
        jobPost.setExpiryDate(jobPostUpdateRequest.getExpiryDate());
        jobPost.setRequirements(jobPostUpdateRequest.getRequirements());
        jobPost.setBenefits(jobPostUpdateRequest.getBenefits());
        jobPost.setAddress(jobPostUpdateRequest.getAddress());

        jobPostRepository.save(jobPost);
    }

    @Override
    public boolean hiddenJobPost(String id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));

        jobPost.setHidden(!jobPost.isHidden());

        jobPostRepository.save(jobPost);

        return jobPost.isHidden();
    }
}
