package com.jobportal.api.mapper;

import com.jobportal.api.dto.job.jobpost.JobPostBasicDTO;
import com.jobportal.api.dto.job.jobpost.JobPostDetailDTO;
import com.jobportal.api.model.job.JobPost;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobPostMapper {

    JobPostBasicDTO mapJobPostToJobPostBasicDTO(JobPost jobPost);

    JobPostDetailDTO mapJobPostToJobPostDetailDTO(JobPost jobPost);
}
