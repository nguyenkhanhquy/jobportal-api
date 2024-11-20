package com.jobportal.api.mapper;

import com.jobportal.api.dto.job.jobapply.JobApplyDTO;
import com.jobportal.api.model.job.JobApply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobApplyMapper {

    @Mapping(source = "jobPost.id", target = "id")
    @Mapping(source = "jobPost.title", target = "title")
    @Mapping(source = "jobPost.jobPosition", target = "jobPosition")
    @Mapping(source = "jobPost.company.name", target = "companyName")
    JobApplyDTO mapJobApplyToJobApplyDTO(JobApply jobApply);
}