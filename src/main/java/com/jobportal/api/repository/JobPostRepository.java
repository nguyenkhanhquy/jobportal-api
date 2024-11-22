package com.jobportal.api.repository;

import com.jobportal.api.model.job.JobPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface JobPostRepository extends MongoRepository<JobPost, String> {

    List<JobPost> findByTitleContainingIgnoreCase(String title);

    Page<JobPost> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<JobPost> findByCompanyIdAndTitleContainingIgnoreCase(String companyId, String title, Pageable pageable);

    Page<JobPost> findByCompanyId(String companyId, Pageable pageable);

    @Query("{'$and': [" +
            "{'isHidden': false}," +
            "{'$or': [" +
            "{'title': {'$regex': ?0, '$options': 'i'}}, " +
            "{'type': {'$regex': ?0, '$options': 'i'}}, " +
            "{'remote': {'$regex': ?0, '$options': 'i'}}, " +
            "{'description': {'$regex': ?0, '$options': 'i'}}, " +
            "{'salary': {'$regex': ?0, '$options': 'i'}}, " +
            "{'companyName': {'$regex': ?0, '$options': 'i'}}, " +
            "{'jobPosition': {'$regex': ?0, '$options': 'i'}}, " +
            "{'address': {'$regex': ?0, '$options': 'i'}} " +
            "]}" +
            "]}")
    Page<JobPost> searchByKeyword(String keyword, Pageable pageable);

    @Query("{'$or': [" +
            "{'title': {'$regex': ?0, '$options': 'i'}}, " +
            "{'type': {'$regex': ?0, '$options': 'i'}}, " +
            "{'remote': {'$regex': ?0, '$options': 'i'}}, " +
            "{'description': {'$regex': ?0, '$options': 'i'}}, " +
            "{'salary': {'$regex': ?0, '$options': 'i'}}, " +
            "{'companyName': {'$regex': ?0, '$options': 'i'}}, " +
            "{'jobPosition': {'$regex': ?0, '$options': 'i'}}, " +
            "{'address': {'$regex': ?0, '$options': 'i'}} " +
            "]}")
    Page<JobPost> searchByKeywordAdmin(String keyword, Pageable pageable);

    Page<JobPost> findAllByisHidden(boolean isHidden, Pageable pageable);
}
