package com.luongchivi.identity_service.mapper;

import com.luongchivi.identity_service.dto.request.role.RoleRequest;
import com.luongchivi.identity_service.dto.response.role.RoleResponse;
import com.luongchivi.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
