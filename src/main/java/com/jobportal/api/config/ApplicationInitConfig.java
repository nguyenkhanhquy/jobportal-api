package com.jobportal.api.config;

import com.jobportal.api.model.user.Role;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.RoleRepository;
import com.jobportal.api.repository.UserRepository;
import com.jobportal.api.task.TokenCleanupTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;
    private final TokenCleanupTask tokenCleanupTask;

    @Autowired
    public ApplicationInitConfig(PasswordEncoder passwordEncoder, TokenCleanupTask tokenCleanupTask) {
        this.passwordEncoder = passwordEncoder;
        this.tokenCleanupTask = tokenCleanupTask;
    }

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            initializeRoles(roleRepository);
            initializeAdminUser(userRepository, roleRepository);
            runTokenCleanupTask();
        };
    }

    private void initializeRoles(RoleRepository roleRepository) {
        if (roleRepository.count() == 0) {
            List<String> roleNames = List.of("ADMIN", "JOB_SEEKER", "RECRUITER");
            roleNames.forEach(roleName ->
                    roleRepository.save(Role.builder()
                            .name(roleName)
                            .build())
            );
        }
    }

    private void initializeAdminUser(UserRepository userRepository, RoleRepository roleRepository) {
        if (userRepository.findByEmail("admin@admin.com") == null) {
            User user = User.builder()
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("12345678"))
                    .isActive(true)
                    .registrationDate(Date.from(Instant.now()))
                    .role(roleRepository.findByName("ADMIN"))
                    .build();
            userRepository.save(user);
        }
    }

    private void runTokenCleanupTask() {
        log.info("Running token cleanup task on startup...");
        tokenCleanupTask.deleteExpiredTokens();
    }
}
