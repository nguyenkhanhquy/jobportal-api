package com.jobportal.api.service;

import com.jobportal.api.dto.profile.JobSeekerProfileDTO;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.request.profile.UpdateInfoJobSeekerRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.JobSeekerProfileMapper;
import com.jobportal.api.model.profile.JobSeekerProfile;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.JobSeekerProfileRepository;
import com.jobportal.api.repository.UserRepository;
import com.jobportal.api.util.AuthUtil;
import com.jobportal.api.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class JobSeekerProfileServiceImpl implements JobSeekerProfileService {

    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final JobSeekerProfileMapper jobSeekerProfileMapper;

    @Autowired
    public JobSeekerProfileServiceImpl(UserRepository userRepository, JobSeekerProfileRepository jobSeekerProfileRepository, JobSeekerProfileMapper jobSeekerProfileMapper) {
        this.userRepository = userRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.jobSeekerProfileMapper = jobSeekerProfileMapper;
    }

    @Override
    public JobSeekerProfile updateAvatar(MultipartFile multipart) {
        User user = AuthUtil.getAuthenticatedUser(userRepository);
        JobSeekerProfile jobSeekerProfile = jobSeekerProfileRepository.findByUser(user);
        if (jobSeekerProfile == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }

        String fileName = multipart.getOriginalFilename();
        // Kiểm tra nếu fileName là null hoặc không có phần mở rộng
        if (fileName == null || !fileName.contains(".")) {
            throw new CustomException(EnumException.INVALID_FILE_NAME);
        }

        try (InputStream inputStream = multipart.getInputStream()) {
            // Tạo tên file mới với phần mở rộng từ file gốc
            String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
            String newFileName = "user-profile/job-seeker/" + user.getId() + "/avatar/avatar_" + System.currentTimeMillis() + fileExtension;

            // Upload tệp lên S3
            S3Util.uploadFile(newFileName, inputStream);

            String avatarUrl = "https://" + S3Util.AWS_BUCKET + ".s3.amazonaws.com/" + newFileName;
            jobSeekerProfile.setAvatar(avatarUrl);
            return jobSeekerProfileRepository.save(jobSeekerProfile);
        } catch (IOException e) {
            throw new CustomException(EnumException.ERROR_UPLOAD_FILE);
        }
    }

    @Override
    public void updateProfile(UpdateInfoJobSeekerRequest updateInfoJobSeekerRequest) {
        User user = AuthUtil.getAuthenticatedUser(userRepository);
        JobSeekerProfile jobSeekerProfile = jobSeekerProfileRepository.findByUser(user);
        if (jobSeekerProfile == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }

        jobSeekerProfile.setFullName(updateInfoJobSeekerRequest.getFullName());
        jobSeekerProfile.setDob(updateInfoJobSeekerRequest.getDob());
        jobSeekerProfile.setPhone(updateInfoJobSeekerRequest.getPhone());
        jobSeekerProfile.setAddress(updateInfoJobSeekerRequest.getAddress());
        jobSeekerProfile.setWorkExperience(updateInfoJobSeekerRequest.getWorkExperience());

        jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    @Override
    public SuccessResponse<List<JobSeekerProfileDTO>> getAllJobSeekers(JobPostSearchFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<JobSeekerProfile> pageData;
        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            pageData = jobSeekerProfileRepository.findByFullNameContainingIgnoreCase(request.getQuery(), pageable);
        } else {
            pageData = jobSeekerProfileRepository.findAll(pageable);
        }

        return SuccessResponse.<List<JobSeekerProfileDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent().stream()
                        .map(jobSeekerProfileMapper::toDTO)
                        .toList())
                .build();
    }
}
