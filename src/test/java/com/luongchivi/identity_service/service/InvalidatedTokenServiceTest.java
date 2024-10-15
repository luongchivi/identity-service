package com.luongchivi.identity_service.service;

import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.luongchivi.identity_service.entity.InvalidatedToken;
import com.luongchivi.identity_service.repository.InvalidatedTokenRepository;

@SpringBootTest
class InvalidatedTokenServiceTest {
    @Autowired
    InvalidatedTokenService invalidatedTokenService;

    @MockBean
    InvalidatedTokenRepository invalidatedTokenRepository;

    @Test
    void testDeleteExpiredTokens_withExpiredTokens() {
        // Arrange: Khởi tạo mock và InjectMocks
        var token1 = new InvalidatedToken("54458180-9906-4e9e-b37a-1a1b579332fc", new Date());
        var token2 = new InvalidatedToken("1ef8c965-f1d5-4d40-b21d-ea684fc1ddb3", new Date());
        when(invalidatedTokenRepository.findByExpiryTimeBefore(any(Date.class))).thenReturn(List.of(token1, token2));

        // Act: Gọi phương thức deleteExpiredTokens()
        invalidatedTokenService.deleteExpiredTokens();

        // Assert: Xác minh rằng deleteAll đã được gọi với danh sách token hết hạn
        verify(invalidatedTokenRepository).deleteAll(List.of(token1, token2));
    }

    @Test
    void testDeleteExpiredTokens_noExpiredTokens() {
        // Arrange: Khởi tạo mock và InjectMocks
        when(invalidatedTokenRepository.findByExpiryTimeBefore(any(Date.class))).thenReturn(List.of());

        // Act: Gọi phương thức deleteExpiredTokens()
        invalidatedTokenService.deleteExpiredTokens();

        // Assert: Xác minh rằng deleteAll không được gọi vì không có token hết hạn
        verify(invalidatedTokenRepository, never()).deleteAll(anyList());
    }
}
