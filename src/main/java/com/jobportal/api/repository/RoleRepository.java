package com.jobportal.api.repository;

import com.jobportal.api.model.user.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByName(String name);
}