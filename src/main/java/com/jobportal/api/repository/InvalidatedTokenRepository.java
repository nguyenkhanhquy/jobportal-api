package com.jobportal.api.repository;

import com.jobportal.api.model.user.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

    int deleteByExpiryTimeBefore(Date now);
}
