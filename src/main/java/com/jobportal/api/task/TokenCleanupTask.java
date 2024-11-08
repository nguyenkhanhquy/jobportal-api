package com.jobportal.api.task;

import com.jobportal.api.repository.InvalidatedTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
@Transactional
public class TokenCleanupTask {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Autowired
    public TokenCleanupTask(InvalidatedTokenRepository invalidatedTokenRepository) {
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    @Scheduled(cron = "0 59 23 * * ?")
    public void deleteExpiredTokens() {
        log.info("Starting expired token cleanup task at {}", Instant.now());
        try {
            int deletedCount = invalidatedTokenRepository.deleteByExpiryTimeBefore(Date.from(Instant.now()));
            log.info("Completed expired token cleanup task. Tokens deleted: {}", deletedCount);
        } catch (Exception e) {
            log.error("Error during token cleanup task: {}", e.getMessage(), e);
        }
    }
}
