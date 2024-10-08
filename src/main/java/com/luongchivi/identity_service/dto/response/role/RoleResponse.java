package com.luongchivi.identity_service.dto.response.role;

import java.util.Set;

import com.luongchivi.identity_service.dto.response.permission.PermissionResponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    String name;
    String description;
    Set<PermissionResponse> permissions;
}
