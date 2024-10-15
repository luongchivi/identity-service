package com.luongchivi.identity_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.luongchivi.identity_service.dto.request.user.UserCreationRequest;
import com.luongchivi.identity_service.dto.request.user.UserUpdateRequest;
import com.luongchivi.identity_service.dto.response.user.UserResponse;
import com.luongchivi.identity_service.service.UserService;
import com.luongchivi.identity_service.share.response.ApiResponse;
import com.luongchivi.identity_service.share.response.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "User")
@Validated
public class UserController {
    UserService userService;

    @Operation(
            summary = "This endpoint create new user",
            responses = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "200")
            })
    @PostMapping()
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse user = userService.createUser(request);
        return ApiResponse.<UserResponse>builder().results(user).build();
    }

    @Operation(summary = "This endpoint get list users")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    public ApiResponse<PageResponse<UserResponse>> getUsers(
            @RequestParam(value = "page", required = false, defaultValue = "1")
                    @Min(value = 1, message = "PAGE_INVALID")
                    int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10")
                    @Min(value = 1, message = "PAGE_SIZE_INVALID")
                    int pageSize,
            @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy) {
        PageResponse<UserResponse> users = userService.getUsers(page, pageSize, sort, sortBy);
        return ApiResponse.<PageResponse<UserResponse>>builder().results(users).build();
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

    @Operation(summary = "This endpoint delete user")
    @DeleteMapping("/{userId}")
    public ApiResponse deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return ApiResponse.builder().message("Delete user successfully.").build();
    }
}
