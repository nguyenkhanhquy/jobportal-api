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
            if (roleRepository.findByName("ADMIN") == null) {
                Role role = Role.builder()
                        .name("ADMIN")
                        .build();

                roleRepository.save(role);
            }

            if (roleRepository.findByName("USER") == null) {
                Role role = Role.builder()
                        .name("USER")
                        .build();

                roleRepository.save(role);
            }

            if (!userRepository.existsByEmail("admin@admin.com")) {
                User user = User.builder()
                        .email("admin@admin.com")
                        .password(passwordEncoder.encode("admin"))
                        .role(roleRepository.findByName("ADMIN"))
                        .build();

                userRepository.save(user);
                log.warn("Admin user created with email: admin@admin.com");
            } else {
                log.info("Admin user already exists");
            }

            log.info("Running token cleanup task on startup...");
            tokenCleanupTask.deleteExpiredTokens();
        };
    }
}
