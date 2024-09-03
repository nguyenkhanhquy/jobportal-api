package com.jobportal.api.config;

import com.jobportal.api.model.user.Role;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.UserRepository;
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

    @Autowired
    public ApplicationInitConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            log.info("Checking if admin user exists...");

            if (!userRepository.existsByEmail("admin@admin.com")) {
                User user = User.builder()
                        .email("admin@admin.com")
                        .password(passwordEncoder.encode("admin"))
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(user);
                log.warn("Admin user created with email: admin@admin.com");
            } else {
                log.info("Admin user already exists");
            }
        };
    }
}
