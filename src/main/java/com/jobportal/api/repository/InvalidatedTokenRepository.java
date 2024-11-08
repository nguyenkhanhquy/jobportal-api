package com.jobportal.api.repository;

import com.jobportal.api.model.user.InvalidatedToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

public interface InvalidatedTokenRepository extends MongoRepository<InvalidatedToken, String> {

    int deleteByExpiryTimeBefore(Date now);
}
