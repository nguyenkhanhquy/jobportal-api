package com.jobportal.api.repository;

import com.jobportal.api.model.job.JobSaved;
import com.jobportal.api.model.profile.JobSeekerProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobSavedRepository extends MongoRepository<JobSaved, String> {

    List<JobSaved> findByJobSeekerProfile(JobSeekerProfile jobSeekerProfile);
}
