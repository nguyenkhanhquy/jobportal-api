package com.jobportal.api.service;

import com.jobportal.api.dto.job.jobapply.JobApplyDTO;
import com.jobportal.api.dto.job.jobapply.JobApplyDetailDTO;
import com.jobportal.api.dto.request.job.CreateApplyJobRequest;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.JobApplyMapper;
import com.jobportal.api.model.job.JobApply;
import com.jobportal.api.model.job.JobPost;
import com.jobportal.api.model.profile.JobSeekerProfile;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.JobApplyRepository;
import com.jobportal.api.repository.JobPostRepository;
import com.jobportal.api.repository.JobSeekerProfileRepository;
import com.jobportal.api.repository.UserRepository;
import com.jobportal.api.util.AuthUtil;
import com.jobportal.api.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class JobApplyServiceImpl implements JobApplyService {

    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final JobPostRepository jobPostRepository;
    private final JobApplyRepository jobApplyRepository;
    private final JobApplyMapper jobApplyMapper;

    @Autowired
    public JobApplyServiceImpl(UserRepository userRepository, JobSeekerProfileRepository jobSeekerProfileRepository, JobPostRepository jobPostRepository, JobApplyRepository jobApplyRepository, JobApplyMapper jobApplyMapper) {
        this.userRepository = userRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.jobPostRepository = jobPostRepository;
        this.jobApplyRepository = jobApplyRepository;
        this.jobApplyMapper = jobApplyMapper;
    }

    @Override
    public void createApplyJob(CreateApplyJobRequest request) {
        User user = AuthUtil.getAuthenticatedUser(userRepository);

        JobSeekerProfile jobSeeker = jobSeekerProfileRepository.findByUser(user);
        if (jobSeeker == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }

        JobPost jobPost = jobPostRepository.findById(request.getJobPostId())
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));

        JobApply jobApply = JobApply.builder()
                .jobPost(jobPost)
                .jobSeekerProfile(jobSeeker)
                .coverLetter(request.getCoverLetter())
                .cv(request.getCv())
                .applyDate(Date.from(Instant.now()))
                .build();

        jobApplyRepository.save(jobApply);

        jobPost.getJobApplies().add(jobApply);

        jobPostRepository.save(jobPost);
    }

    @Override
    public String uploadCV(MultipartFile multipart) {
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
            String newFileName = "cv/" + user.getId() + "/cv" + System.currentTimeMillis() + fileExtension;

            // Upload tệp lên S3
            S3Util.uploadFile(newFileName, inputStream);

            return "https://" + S3Util.AWS_BUCKET + ".s3.amazonaws.com/" + newFileName;
        } catch (IOException e) {
            throw new CustomException(EnumException.ERROR_UPLOAD_FILE);
        }
    }

    @Override
    public SuccessResponse<List<JobApplyDTO>> getJobApplyByJobSeekerProfile(JobPostSearchFilterRequest request) {
        User user = AuthUtil.getAuthenticatedUser(userRepository);

        JobSeekerProfile jobSeekerProfile = jobSeekerProfileRepository.findByUser(user);
        if (jobSeekerProfile == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }

        List<JobApply> jobApplies = jobApplyRepository.findByJobSeekerProfile(jobSeekerProfile);

        List<JobApplyDTO> jobApplyDTOs = jobApplies.stream()
                .map(jobApplyMapper::mapJobApplyToJobApplyDTO)
                .filter(job -> {
                    String search = request.getQuery();
                    if (search == null || search.trim().isEmpty()) return true;

                    String searchLower = search.toLowerCase().trim();
                    return (job.getTitle() != null && job.getTitle().toLowerCase().contains(searchLower)) ||
                            (job.getJobPosition() != null && job.getJobPosition().toLowerCase().contains(searchLower)) ||
                            (job.getCompanyName() != null && job.getCompanyName().toLowerCase().contains(searchLower));
                })
                .sorted((a, b) -> b.getApplyDate().compareTo(a.getApplyDate())) // Sắp xếp theo thời gian ứng tuyển mới nhất (Giảm dần)
                .toList();

        int filteredTotalElements = jobApplyDTOs.size();
        int filteredTotalPages = (int) Math.ceil((double) filteredTotalElements / request.getSize());

        // Xác định kích thước trang và số trang từ request
        int pageNumber = request.getPage() - 1; // 0-based index
        int pageSize = request.getSize();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, jobApplyDTOs.size());

        // Kiểm tra điều kiện phân trang để tránh lỗi khi startIndex vượt quá kích thước của List
        List<JobApplyDTO> pageContent;
        if (startIndex < jobApplyDTOs.size()) {
            pageContent = jobApplyDTOs.subList(startIndex, endIndex);
        } else {
            pageContent = Collections.emptyList();
        }

        Page<JobApplyDTO> pageData = new PageImpl<>(
                pageContent,
                PageRequest.of(pageNumber, pageSize),
                filteredTotalElements
        );

        return SuccessResponse.<List<JobApplyDTO>>builder()
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
    public List<JobApplyDetailDTO> getALlJobApplyByJobPostId(String id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));

        return jobPost.getJobApplies().stream()
                .map(jobApplyMapper::toDetailDTO)
                .toList();
    }
}
