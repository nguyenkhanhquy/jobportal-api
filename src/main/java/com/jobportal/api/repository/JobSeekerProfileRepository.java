package com.jobportal.api.repository;

import com.jobportal.api.model.profile.JobSeekerProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobSeekerProfileRepository extends MongoRepository<JobSeekerProfile, String> {

}
