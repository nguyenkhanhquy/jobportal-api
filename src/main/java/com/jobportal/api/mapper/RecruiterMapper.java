package com.jobportal.api.mapper;

import com.jobportal.api.dto.business.RecruiterDTO;
import com.jobportal.api.model.profile.RecruiterProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecruiterMapper {

    RecruiterDTO mapRecruiterToRecruiterDTO(RecruiterProfile recruiter);
}
