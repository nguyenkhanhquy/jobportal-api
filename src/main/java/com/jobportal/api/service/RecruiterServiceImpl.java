package com.jobportal.api.service;

import com.jobportal.api.dto.business.RecruiterDTO;
import com.jobportal.api.dto.request.profile.UpdateRecruiterProfileRequest;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.mapper.RecruiterMapper;
import com.jobportal.api.model.profile.Company;
import com.jobportal.api.model.profile.RecruiterProfile;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.CompanyRepository;
import com.jobportal.api.repository.RecruiterProfileRepository;
import com.jobportal.api.repository.UserRepository;
import com.jobportal.api.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterRepository;
    private final RecruiterMapper recruiterMapper;

    @Autowired
    public RecruiterServiceImpl(CompanyRepository companyRepository, UserRepository userRepository, RecruiterProfileRepository recruiterRepository, RecruiterMapper recruiterMapper) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.recruiterRepository = recruiterRepository;
        this.recruiterMapper = recruiterMapper;
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
}
