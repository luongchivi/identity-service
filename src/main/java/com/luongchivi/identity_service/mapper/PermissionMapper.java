package com.luongchivi.identity_service.mapper;

import com.luongchivi.identity_service.dto.request.permission.PermissionCreationRequest;
import org.mapstruct.Mapper;

import com.luongchivi.identity_service.dto.response.permission.PermissionResponse;
import com.luongchivi.identity_service.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreationRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
