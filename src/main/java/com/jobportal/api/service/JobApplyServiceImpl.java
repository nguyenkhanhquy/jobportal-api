package com.jobportal.api.service;

import com.jobportal.api.dto.request.job.CreateApplyJobRequest;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class JobApplyServiceImpl implements JobApplyService {

    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final JobPostRepository jobPostRepository;
    private final JobApplyRepository jobApplyRepository;

    @Autowired
    public JobApplyServiceImpl(UserRepository userRepository, JobSeekerProfileRepository jobSeekerProfileRepository, JobPostRepository jobPostRepository, JobApplyRepository jobApplyRepository) {
        this.userRepository = userRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.jobPostRepository = jobPostRepository;
        this.jobApplyRepository = jobApplyRepository;
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
}
