package com.jobportal.api.repository;

import com.jobportal.api.model.job.JobPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobPostRepository extends MongoRepository<JobPost, String> {

    List<JobPost> findByTitleContainingIgnoreCase(String title);

    Page<JobPost> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
