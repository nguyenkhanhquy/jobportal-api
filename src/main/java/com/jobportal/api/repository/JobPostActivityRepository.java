package com.jobportal.api.repository;

import com.jobportal.api.model.job.JobPostActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostActivityRepository extends JpaRepository<JobPostActivity, String> {

    Page<JobPostActivity> findByTitleContaining(String title, Pageable pageable);
}
