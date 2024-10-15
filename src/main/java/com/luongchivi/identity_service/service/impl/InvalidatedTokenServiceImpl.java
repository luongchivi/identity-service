package com.luongchivi.identity_service.service.impl;

import java.util.Date;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.luongchivi.identity_service.repository.InvalidatedTokenRepository;
import com.luongchivi.identity_service.service.InvalidatedTokenService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvalidatedTokenServiceImpl implements InvalidatedTokenService {

    InvalidatedTokenRepository invalidatedTokenRepository;

    @Transactional
    public void deleteExpiredTokens() {
        var expiredTokens = invalidatedTokenRepository.findByExpiryTimeBefore(new Date());
        if (!expiredTokens.isEmpty()) {
            invalidatedTokenRepository.deleteAll(expiredTokens);
            log.info("Deleted {} expired tokens", expiredTokens.size());
        } else {
            log.info("None expired tokens");
        }
    }
}
