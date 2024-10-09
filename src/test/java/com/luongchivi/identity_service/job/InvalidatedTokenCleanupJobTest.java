package com.luongchivi.identity_service.job;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.luongchivi.identity_service.service.InvalidatedTokenService;

@SpringBootTest
public class InvalidatedTokenCleanupJobTest {
    @Autowired
    private InvalidatedTokenCleanupJob invalidatedTokenCleanupJob;

    @MockBean
    private InvalidatedTokenService invalidatedTokenService;

    @Test
    void cleanExpiredTokens_success() {
        invalidatedTokenCleanupJob.cleanExpiredTokens();
        verify(invalidatedTokenService).deleteExpiredTokens();
    }

    @Test
    void cleanExpiredTokens_hasException_failed() {
        doThrow(new RuntimeException("Error")).when(invalidatedTokenService).deleteExpiredTokens();
        invalidatedTokenCleanupJob.cleanExpiredTokens();
    }
}
