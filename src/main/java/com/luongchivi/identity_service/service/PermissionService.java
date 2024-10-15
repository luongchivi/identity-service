package com.luongchivi.identity_service.service;

import java.util.List;

import com.luongchivi.identity_service.dto.request.permission.PermissionCreationRequest;
import com.luongchivi.identity_service.dto.response.permission.PermissionResponse;

public interface PermissionService {
    PermissionResponse createPermission(PermissionCreationRequest request);

    List<PermissionResponse> getPermissions();

    void deletePermission(String permissionName);
}
