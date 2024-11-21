package com.jobportal.api.service;

import com.jobportal.api.dto.business.RecruiterDTO;
import com.jobportal.api.dto.profile.RecruiterProfileDTO;
import com.jobportal.api.dto.request.job.JobPostSearchFilterRequest;
import com.jobportal.api.dto.request.profile.UpdateRecruiterProfileRequest;
import com.jobportal.api.dto.response.SuccessResponse;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.RecruiterMapper;
import com.jobportal.api.mapper.RecruiterProfileMapper;
import com.jobportal.api.model.profile.Company;
import com.jobportal.api.model.profile.RecruiterProfile;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.CompanyRepository;
import com.jobportal.api.repository.RecruiterProfileRepository;
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
public class RecruiterServiceImpl implements RecruiterService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterRepository;
    private final RecruiterMapper recruiterMapper;
    private final RecruiterProfileMapper recruiterProfileMapper;

    @Autowired
    public RecruiterServiceImpl(CompanyRepository companyRepository, UserRepository userRepository, RecruiterProfileRepository recruiterRepository, RecruiterMapper recruiterMapper, RecruiterProfileMapper recruiterProfileMapper) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.recruiterRepository = recruiterRepository;
        this.recruiterMapper = recruiterMapper;
        this.recruiterProfileMapper = recruiterProfileMapper;
    }

    @Override
    public List<RecruiterDTO> getAllRecruiters() {
        List<RecruiterProfile> recruiters = recruiterRepository.findAll();

        return recruiters.stream()
                .map(recruiterMapper::mapRecruiterToRecruiterDTO)
                .toList();
    }

    @Override
    public RecruiterDTO getRecruiterById(String id) {
        RecruiterProfile recruiter = recruiterRepository.findById(id).orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        return recruiterMapper.mapRecruiterToRecruiterDTO(recruiter);
    }

    @Override
    public void updateRecruiterProfile(UpdateRecruiterProfileRequest request) {
        User user = AuthUtil.getAuthenticatedUser(userRepository);

        RecruiterProfile recruiter = recruiterRepository.findByUser(user);
        if (recruiter == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }

        Company company = recruiter.getCompany();
        company.setWebsite(request.getWebsite());
        company.setDescription(request.getDescription());
        company.setAddress(request.getCompanyAddress());
        company.setLogo(request.getCompanyLogo());

        companyRepository.save(company);

        recruiter.setCompany(company);
        recruiter.setName(request.getName());
        recruiter.setPosition(request.getPosition());
        recruiter.setPhone(request.getPhone());
        recruiter.setRecruiterEmail(request.getRecruiterEmail());

        recruiterRepository.save(recruiter);
    }

    @Override
    public String uploadLogo(MultipartFile multipart) {
        User user = AuthUtil.getAuthenticatedUser(userRepository);

        RecruiterProfile recruiter = recruiterRepository.findByUser(user);
        if (recruiter == null) {
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
            String newFileName = "company/" + user.getId() + "/logo/logo_" + System.currentTimeMillis() + fileExtension;

            // Upload tệp lên S3
            S3Util.uploadFile(newFileName, inputStream);

            return "https://" + S3Util.AWS_BUCKET + ".s3.amazonaws.com/" + newFileName;
        } catch (IOException e) {
            throw new CustomException(EnumException.ERROR_UPLOAD_FILE);
        }
    }

    @Override
    public SuccessResponse<List<RecruiterProfileDTO>> getAllRecruiters(JobPostSearchFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<RecruiterProfile> pageData;
        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            pageData = recruiterRepository.findByNameContainingIgnoreCase(request.getQuery(), pageable);
        } else {
            pageData = recruiterRepository.findAll(pageable);
        }

        return SuccessResponse.<List<RecruiterProfileDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent().stream()
                        .map(recruiterProfileMapper::toDTO)
                        .toList())
                .build();
    }
}
