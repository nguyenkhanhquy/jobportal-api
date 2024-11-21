package com.jobportal.api.mapper;

import com.jobportal.api.dto.profile.RecruiterProfileDTO;
import com.jobportal.api.model.profile.RecruiterProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecruiterProfileMapper {

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.active", target = "active")
    @Mapping(source = "user.locked", target = "locked")
    @Mapping(source = "user.registrationDate", target = "registrationDate")
    RecruiterProfileDTO toDTO(RecruiterProfile recruiterProfile);
}
