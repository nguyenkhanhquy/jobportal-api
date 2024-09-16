package com.jobportal.api.repository;

import com.jobportal.api.model.job.JobPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostActivityRepository extends JpaRepository<JobPostActivity, String> {

}
