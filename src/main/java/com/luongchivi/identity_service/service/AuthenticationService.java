package com.luongchivi.identity_service.service;

import java.text.ParseException;

import com.luongchivi.identity_service.dto.request.introspect.IntrospectRequest;
import com.luongchivi.identity_service.dto.response.introspect.IntrospectResponse;
import com.luongchivi.identity_service.dto.request.authentication.AuthenticationRequest;
import com.luongchivi.identity_service.dto.response.authentication.AuthenticationResponse;
import com.luongchivi.identity_service.dto.request.logout.LogoutRequest;
import com.luongchivi.identity_service.dto.request.refresh.RefreshRequest;
import com.nimbusds.jose.JOSEException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
