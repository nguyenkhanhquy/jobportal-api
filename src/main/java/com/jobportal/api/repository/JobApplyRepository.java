package com.jobportal.api.repository;

import com.jobportal.api.model.job.JobApply;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobApplyRepository extends MongoRepository<JobApply, String> {

}
