package com.luongchivi.identity_service.controller;

import com.luongchivi.identity_service.dto.request.role.RoleRequest;
import com.luongchivi.identity_service.dto.response.role.RoleResponse;
import com.luongchivi.identity_service.service.RoleService;
import com.luongchivi.identity_service.share.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleController {
    RoleService roleService;

    @PostMapping()
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        RoleResponse role = roleService.createRole(request);
        return ApiResponse.<RoleResponse>builder()
                .results(role)
                .build();
    }

    @GetMapping()
    public ApiResponse<List<RoleResponse>> getRoles() {
        List<RoleResponse> roles = roleService.getRoles();
        return ApiResponse.<List<RoleResponse>>builder()
                .results(roles)
                .build();
    }

    @DeleteMapping("/{roleName}")
    public ApiResponse deleteRole(@PathVariable("roleName") String roleName) {
        roleService.deleteRole(roleName);
        return ApiResponse.builder()
                .message("Delete role successfully.")
                .build();
    }
}
