package com.luongchivi.identity_service.service;

import com.luongchivi.identity_service.dto.request.role.RoleRequest;
import com.luongchivi.identity_service.dto.response.role.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest request);

    List<RoleResponse> getRoles();

    void deleteRole(String roleName);
}
