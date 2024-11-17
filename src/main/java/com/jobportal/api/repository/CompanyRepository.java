package com.jobportal.api.repository;

import com.jobportal.api.model.profile.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {

    boolean existsByName(String name);
}
