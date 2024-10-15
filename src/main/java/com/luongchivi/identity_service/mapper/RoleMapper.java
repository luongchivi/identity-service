package com.luongchivi.identity_service.mapper;

import com.luongchivi.identity_service.dto.request.role.RoleCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.luongchivi.identity_service.dto.response.role.RoleResponse;
import com.luongchivi.identity_service.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest request);

    RoleResponse toRoleResponse(Role role);
}
