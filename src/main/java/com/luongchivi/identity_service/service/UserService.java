package com.luongchivi.identity_service.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luongchivi.identity_service.dto.request.user.UserCreationRequest;
import com.luongchivi.identity_service.dto.request.user.UserUpdateRequest;
import com.luongchivi.identity_service.dto.response.user.UserResponse;
import com.luongchivi.identity_service.entity.Role;
import com.luongchivi.identity_service.entity.User;
import com.luongchivi.identity_service.exception.AppException;
import com.luongchivi.identity_service.exception.ErrorCode;
import com.luongchivi.identity_service.mapper.UserMapper;
import com.luongchivi.identity_service.repository.RoleRepository;
import com.luongchivi.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        // Map request to user entity
        User user = userMapper.toUser(request);

        // Encode the user's password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Fetch the "User" role
        Role userRole = roleRepository.findById("User").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Assign the "User" role to the user
        user.setRoles(Set.of(userRole));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTED);
        }

        // Save the user and return the response
        return userMapper.toUserResponse(user);
    }

    // hasRole dùng để check ROLE
    // @PreAuthorize("hasRole('Admin')")
    // hasAuthority dùng để check PERMISSION
    // dùng hasAnyAuthority("ROLE_Admin", "read") để check vừa có ROLE là ROLE_Admin, vừa có PERMISSION là read
    @PreAuthorize("hasAuthority('read')")
    public List<UserResponse> getUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        List<GrantedAuthority> roles = authentication.getAuthorities().stream().collect(Collectors.toList());
        log.info("Roles: {}", roles);
        return userRepository.findAll().stream()
                .map(user -> userMapper.toUserResponse(user))
                .toList();
    }

    public UserResponse getUserInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
