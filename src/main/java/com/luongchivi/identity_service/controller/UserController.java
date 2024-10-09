package com.luongchivi.identity_service.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.luongchivi.identity_service.dto.request.user.UserCreationRequest;
import com.luongchivi.identity_service.dto.request.user.UserUpdateRequest;
import com.luongchivi.identity_service.dto.response.user.UserResponse;
import com.luongchivi.identity_service.service.UserService;
import com.luongchivi.identity_service.share.response.ApiResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {
    UserService userService;

    @Operation(
            summary = "This endpoint create new user",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    @PostMapping()
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse user = userService.createUser(request);
        return ApiResponse.<UserResponse>builder().results(user).build();
    }

    @Operation(summary = "This endpoint get list users")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    public ApiResponse<List<UserResponse>> getUsers() {
        List<UserResponse> users = userService.getUsers();
        return ApiResponse.<List<UserResponse>>builder().results(users).build();
    }

    @Operation(summary = "This endpoint get user details information")
    @GetMapping("/info")
    public ApiResponse<UserResponse> getUserInfo() {
        UserResponse userInfo = userService.getUserInfo();
        return ApiResponse.<UserResponse>builder().results(userInfo).build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        UserResponse user = userService.getUser(userId);
        return ApiResponse.<UserResponse>builder().results(user).build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        UserResponse user = userService.updateUser(userId, request);
        return ApiResponse.<UserResponse>builder().results(user).build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return ApiResponse.builder().message("Delete user successfully.").build();
    }
}
