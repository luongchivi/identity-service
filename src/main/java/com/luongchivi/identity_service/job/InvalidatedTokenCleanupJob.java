package com.luongchivi.identity_service.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.luongchivi.identity_service.service.InvalidatedTokenService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvalidatedTokenCleanupJob {
    InvalidatedTokenService invalidatedTokenService;

    // Cron job chạy mỗi ngày vào 3 giờ sáng để xóa token hết hạn
    // @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 0 3 * * ?") // 3 giờ sáng mỗi ngày
    public void cleanExpiredTokens() {
        try {
            log.info("Running token cleanup job");
            invalidatedTokenService.deleteExpiredTokens();
            log.info("Cleanup expired tokens completed");
        } catch (Exception e) {
            log.error("Failed to run token cleanup job", e);
        }
    }
}
