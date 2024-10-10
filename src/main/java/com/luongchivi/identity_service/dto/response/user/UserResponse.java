package com.luongchivi.identity_service.dto.response.user;

import java.time.LocalDate;
import java.util.Set;

import com.luongchivi.identity_service.dto.response.role.RoleResponse;

import com.luongchivi.identity_service.share.response.BaseResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse extends BaseResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    Set<RoleResponse> roles;
}
