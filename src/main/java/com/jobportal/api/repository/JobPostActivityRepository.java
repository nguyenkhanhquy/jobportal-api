package com.jobportal.api.repository;

import com.jobportal.api.model.job.JobPostActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobPostActivityRepository extends MongoRepository<JobPostActivity, String> {

    Page<JobPostActivity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<JobPostActivity> findByAddressContainingIgnoreCase(String address, Pageable pageable);

    Page<JobPostActivity> findByTitleContainingIgnoreCaseAndAddress(String title, String address, Pageable pageable);
}
