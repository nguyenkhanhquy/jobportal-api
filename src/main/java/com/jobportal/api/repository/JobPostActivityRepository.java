package com.jobportal.api.repository;

import com.jobportal.api.model.job.JobPostActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobPostActivityRepository extends JpaRepository<JobPostActivity, String> {

    Page<JobPostActivity> findByTitleContaining(String title, Pageable pageable);

    @Query("SELECT j FROM JobPostActivity j WHERE (:title IS NULL OR j.title LIKE %:title%) AND (:address IS NULL OR j.address = :address)")
    Page<JobPostActivity> findByTitleAndAddress(@Param("title") String title, @Param("address") String address, Pageable pageable);
}
