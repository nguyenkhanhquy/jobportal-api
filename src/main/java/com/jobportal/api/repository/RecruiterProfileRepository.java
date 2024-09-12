package com.jobportal.api.repository;

import com.jobportal.api.model.profile.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, String> {

}
