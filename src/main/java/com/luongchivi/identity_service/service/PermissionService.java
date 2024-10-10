package com.luongchivi.identity_service.service;

import com.luongchivi.identity_service.dto.request.permission.PermissionRequest;
import com.luongchivi.identity_service.dto.response.permission.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest request);

    List<PermissionResponse> getPermissions();

    void deletePermission(String permissionName);
}
