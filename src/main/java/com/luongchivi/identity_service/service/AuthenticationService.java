package com.luongchivi.identity_service.service;

import com.luongchivi.identity_service.dto.request.Introspect.IntrospectRequest;
import com.luongchivi.identity_service.dto.request.Introspect.IntrospectResponse;
import com.luongchivi.identity_service.dto.request.authentication.AuthenticationRequest;
import com.luongchivi.identity_service.dto.request.authentication.AuthenticationResponse;
import com.luongchivi.identity_service.dto.request.logout.LogoutRequest;
import com.luongchivi.identity_service.dto.request.refresh.RefreshRequest;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;


public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request);

    void logout(LogoutRequest request);

    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
