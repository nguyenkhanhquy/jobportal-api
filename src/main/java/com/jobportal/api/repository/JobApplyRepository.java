package com.jobportal.api.repository;

import com.jobportal.api.model.job.JobApply;
import com.jobportal.api.model.profile.JobSeekerProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobApplyRepository extends MongoRepository<JobApply, String> {

    List<JobApply> findByJobSeekerProfile(JobSeekerProfile jobSeekerProfile);
}
