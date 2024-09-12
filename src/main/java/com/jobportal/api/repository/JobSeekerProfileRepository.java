package com.jobportal.api.repository;

import com.jobportal.api.model.profile.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, String> {

}
