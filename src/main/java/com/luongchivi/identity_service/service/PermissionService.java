package com.luongchivi.identity_service.service;

import com.luongchivi.identity_service.dto.request.permission.PermissionRequest;
import com.luongchivi.identity_service.dto.response.permission.PermissionResponse;
import com.luongchivi.identity_service.entity.Permission;
import com.luongchivi.identity_service.exception.AppException;
import com.luongchivi.identity_service.exception.ErrorCode;
import com.luongchivi.identity_service.mapper.PermissionMapper;
import com.luongchivi.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionService {
    PermissionRepository permissionRepository;

    PermissionMapper permissionMapper;
    public PermissionResponse createPermission(PermissionRequest request) {
        boolean existsByPermission = permissionRepository.existsById(request.getName());
        if (existsByPermission) {
            throw new AppException(ErrorCode.PERMISSION_ALREADY_EXISTED);
        }

        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public List<PermissionResponse> getPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permission -> permissionMapper.toPermissionResponse(permission)).toList();
    }

    public void deletePermission(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }
}
