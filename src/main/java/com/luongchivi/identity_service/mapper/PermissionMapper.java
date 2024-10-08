package com.luongchivi.identity_service.mapper;

import org.mapstruct.Mapper;

import com.luongchivi.identity_service.dto.request.permission.PermissionRequest;
import com.luongchivi.identity_service.dto.response.permission.PermissionResponse;
import com.luongchivi.identity_service.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
