package com.luongchivi.identity_service.controller;

import com.luongchivi.identity_service.dto.request.user.UserCreationRequest;
import com.luongchivi.identity_service.dto.request.user.UserUpdateRequest;
import com.luongchivi.identity_service.dto.response.UserResponse;
import com.luongchivi.identity_service.entity.User;
import com.luongchivi.identity_service.service.UserService;
import com.luongchivi.identity_service.share.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;

    @PostMapping()
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResults(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public String updateUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return "Delete user successfully.";
    }
}
