package com.jobportal.api.repository;

import com.jobportal.api.model.profile.JobSeekerProfile;
import com.jobportal.api.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobSeekerProfileRepository extends MongoRepository<JobSeekerProfile, String> {

    JobSeekerProfile findByUser(User user);

    Page<JobSeekerProfile> findByFullNameContainingIgnoreCase(String query, Pageable pageable);
}
