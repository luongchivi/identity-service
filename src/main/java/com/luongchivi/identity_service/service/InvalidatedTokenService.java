package com.luongchivi.identity_service.service;

public interface InvalidatedTokenService {
    void deleteExpiredTokens();
}
