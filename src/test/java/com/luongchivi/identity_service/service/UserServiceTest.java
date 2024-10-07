package com.luongchivi.identity_service.service;

import com.luongchivi.identity_service.dto.request.user.UserCreationRequest;
import com.luongchivi.identity_service.dto.response.user.UserResponse;
import com.luongchivi.identity_service.entity.Role;
import com.luongchivi.identity_service.entity.User;
import com.luongchivi.identity_service.exception.AppException;
import com.luongchivi.identity_service.exception.ErrorCode;
import com.luongchivi.identity_service.repository.RoleRepository;
import com.luongchivi.identity_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;
    private UserCreationRequest request;
    private User user;
    private LocalDate dateOfBirth;
    private Role role;

    @BeforeEach
    void initData() {
        dateOfBirth = LocalDate.of(1999, 3, 6);

        request = UserCreationRequest.builder()
                .username("luongchivi060399")
                .firstName("Vi")
                .lastName("Luong Chi")
                .password("12345678")
                .dateOfBirth(dateOfBirth)
                .build();

        user = User.builder()
                .id("e570ddff-76fc-4fb0-adf0-8979a57d3d76")
                .username("luongchivi060399")
                .firstName("Vi")
                .lastName("Luong Chi")
                .dateOfBirth(dateOfBirth)
                .build();

        role = Role.builder()
                .name("User")
                .description("User role description")
                .build();
    }

    @Test
    public void createUser_validRequest_success() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(roleRepository.findById("User")).thenReturn(Optional.of(role));
        when(userRepository.save(any())).thenReturn(user);

        UserResponse userResponse = userService.createUser(request);

        assertThat(userResponse.getId()).isEqualTo("e570ddff-76fc-4fb0-adf0-8979a57d3d76");
        assertThat(userResponse.getUsername()).isEqualTo("luongchivi060399");
    }

    @Test
    public void createUser_userExisted_failed() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }

    @Test
    @WithMockUser(username = "luongchivi060399")
    public void getUserInfo_validRequest_success() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserResponse userResponse = userService.getUserInfo();
        assertThat(userResponse.getId()).isEqualTo("e570ddff-76fc-4fb0-adf0-8979a57d3d76");
        assertThat(userResponse.getUsername()).isEqualTo("luongchivi060399");
    }

    @Test
    @WithMockUser(username = "luongchivi060399")
    public void getUserInfo_userNotExisted_failed() {
        // Khi `userRepository.findByUsername` không tìm thấy người dùng, nó sẽ ném ra AppException
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        // Kiểm tra xem `AppException` có bị ném ra không
        var exception = assertThrows(AppException.class, () -> userService.getUserInfo());

        // Kiểm tra mã lỗi của AppException
        assertThat(exception.getErrorCode().getCode()).isEqualTo(1003);
        assertThat(exception.getMessage()).isEqualTo("User not found");
    }
}
