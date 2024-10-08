package com.luongchivi.identity_service.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.luongchivi.identity_service.repository.InvalidatedTokenRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvalidatedTokenService {

    InvalidatedTokenRepository invalidatedTokenRepository;

    public void deleteExpiredTokens() {
        Date now = new Date();
        var expiredTokens = invalidatedTokenRepository.findByExpiryTimeBefore(now);
        if (!expiredTokens.isEmpty()) {
            invalidatedTokenRepository.deleteAll(expiredTokens);
            log.info("Deleted " + expiredTokens.size() + " expired tokens.");
        }
    }
}
