package com.jobportal.api.service;

import com.jobportal.api.dto.job.jobpost.JobPostBasicDTO;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.JobPostMapper;
import com.jobportal.api.model.job.JobPost;
import com.jobportal.api.model.job.JobSaved;
import com.jobportal.api.model.profile.JobSeekerProfile;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.JobPostRepository;
import com.jobportal.api.repository.JobSavedRepository;
import com.jobportal.api.repository.JobSeekerProfileRepository;
import com.jobportal.api.repository.UserRepository;
import com.jobportal.api.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobSavedServiceImpl implements JobSavedService {

    private final UserRepository userRepository;
    private final JobSavedRepository jobSavedRepository;
    private final JobPostRepository jobPostRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final JobPostMapper jobPostMapper;

    @Autowired
    public JobSavedServiceImpl(UserRepository userRepository, JobSavedRepository jobSavedRepository, JobPostRepository jobPostRepository, JobSeekerProfileRepository jobSeekerProfileRepository, JobPostMapper jobPostMapper) {
        this.userRepository = userRepository;
        this.jobSavedRepository = jobSavedRepository;
        this.jobPostRepository = jobPostRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.jobPostMapper = jobPostMapper;
    }

    @Override
    public SuccessResponse<List<JobPostBasicDTO>> getAllSavedJobPosts(JobPostSearchFilterRequest request) {
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

        List<JobPost> jobPosts;

        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            jobPosts = jobPostRepository.findByTitleContainingIgnoreCase(request.getQuery());
        } else {
            jobPosts = jobPostRepository.findAll();
        }


        List<JobPostBasicDTO> jobPostBasic = jobPosts.stream()
                .map(jobPostMapper::mapJobPostToJobPostBasicDTO)
                .filter(dto -> savedJobPostIds.contains(dto.getId())) // Chỉ giữ lại những DTO có jobPostId nằm trong savedJobPostIds
                .toList();

        int filteredTotalElements = jobPostBasic.size();
        int filteredTotalPages = (int) Math.ceil((double) filteredTotalElements / request.getSize());

        // Xác định kích thước trang và số trang từ request
        int pageNumber = request.getPage() - 1; // 0-based index
        int pageSize = request.getSize();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, jobPostBasic.size());

        // Kiểm tra điều kiện phân trang để tránh lỗi khi startIndex vượt quá kích thước của List
        List<JobPostBasicDTO> pageContent;
        if (startIndex < jobPostBasic.size()) {
            pageContent = jobPostBasic.subList(startIndex, endIndex);
        } else {
            pageContent = Collections.emptyList();
        }

        // Tạo đối tượng Page từ danh sách đã được phân trang
        Page<JobPostBasicDTO> pageData = new PageImpl<>(pageContent, PageRequest.of(pageNumber, pageSize), filteredTotalElements);

        return SuccessResponse.<List<JobPostBasicDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(filteredTotalPages)
                        .pageSize(request.getSize())
                        .totalElements(filteredTotalElements)
                        .hasPreviousPage(request.getPage() > 1)
                        .hasNextPage(request.getPage() < filteredTotalPages)
                        .build())
                .result(pageData.getContent())
                .build();
    }

    @Override
    public void deleteAllSavedJobPosts() {
        User user = AuthUtil.getAuthenticatedUser(userRepository);

        JobSeekerProfile jobSeeker = jobSeekerProfileRepository.findByUser(user);
        if (jobSeeker == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }

        jobSavedRepository.deleteAllByJobSeekerProfile(jobSeeker);
    }
}
