package com.luongchivi.identity_service.service;

import com.luongchivi.identity_service.dto.request.user.UserCreationRequest;
import com.luongchivi.identity_service.dto.request.user.UserUpdateRequest;
import com.luongchivi.identity_service.dto.response.user.UserResponse;
import com.luongchivi.identity_service.share.response.PageResponse;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);

    PageResponse<UserResponse> getUsers(int page, int pageSize, String sortDirection, String sortBy);

    UserResponse getUserInfo();

    UserResponse getUser(String userId);

    UserResponse updateUser(String userId, UserUpdateRequest request);

    void deleteUser(String userId);
}
