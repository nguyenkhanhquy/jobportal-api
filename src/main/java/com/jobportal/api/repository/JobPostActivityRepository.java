package com.jobportal.api.repository;

import com.jobportal.api.model.job.JobPostActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface JobPostActivityRepository extends MongoRepository<JobPostActivity, String> {

    // Tìm kiếm theo title
    Page<JobPostActivity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Sử dụng cú pháp @Query của MongoDB cho các điều kiện lọc
    @Query("{ $and: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'address': ?1 } ] }")
    Page<JobPostActivity> findByTitleAndAddress(String title, String address, Pageable pageable);
}
