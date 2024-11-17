package com.jobportal.api.repository;

import com.jobportal.api.model.profile.RecruiterProfile;
import com.jobportal.api.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecruiterProfileRepository extends MongoRepository<RecruiterProfile, String> {

    RecruiterProfile findByUser(User user);
}
