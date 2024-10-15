package com.luongchivi.identity_service.service;

import java.util.List;

import com.luongchivi.identity_service.dto.request.role.RoleCreationRequest;
import com.luongchivi.identity_service.dto.response.role.RoleResponse;

public interface RoleService {
    RoleResponse createRole(RoleCreationRequest request);

    List<RoleResponse> getRoles();

    void deleteRole(String roleName);
}
