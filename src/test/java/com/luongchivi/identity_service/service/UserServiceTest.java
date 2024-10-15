package com.luongchivi.identity_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.luongchivi.identity_service.dto.request.user.UserCreationRequest;
import com.luongchivi.identity_service.dto.response.user.UserResponse;
import com.luongchivi.identity_service.entity.Role;
import com.luongchivi.identity_service.entity.User;
import com.luongchivi.identity_service.exception.AppException;
import com.luongchivi.identity_service.repository.RoleRepository;
import com.luongchivi.identity_service.repository.UserRepository;

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

        role = Role.builder().name("User").description("User role description").build();
    }

    @Test
    public void createUser_validRequest_success() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(roleRepository.findById("User")).thenReturn(Optional.of(role));
        when(userRepository.save(any())).thenReturn(user);

        UserResponse userResponse = userService.createUser(request);

        assertThat(userResponse.getId()).isEqualTo("e570ddff-76fc-4fb0-adf0-8979a57d3d76");
        assertThat(userResponse.getUsername()).isEqualTo("luongchivi060399");
    }

    @Test
    public void createUser_userExisted_failed() {
        when(roleRepository.findById("User")).thenReturn(Optional.of(role));

        // Giả lập việc ném ra DataIntegrityViolationException khi save user
        when(userRepository.save(any()))
                .thenThrow(
                        new DataIntegrityViolationException("Duplicate entry 'luongchivi060399' for key 'username'"));

        // Kiểm tra xem AppException có bị ném ra không
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        // Kiểm tra mã lỗi của AppException
        assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
        assertThat(exception.getMessage()).isEqualTo("User already existed");
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

    @Test
    @WithMockUser(username = "luongchivi060399")
    void getUser_validRequest_success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponse userResponse = userService.getUser(user.getId());
        assertThat(userResponse.getId()).isEqualTo("e570ddff-76fc-4fb0-adf0-8979a57d3d76");
        assertThat(userResponse.getUsername()).isEqualTo("luongchivi060399");
    }

    @Test
    @WithMockUser(username = "luongchivi060399")
    public void getUser_userNotExisted_failed() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class, () -> userService.getUser(user.getId()));

        assertThat(exception.getErrorCode().getCode()).isEqualTo(1003);
        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    @WithMockUser(username = "Admin", authorities = "ROLE_Admin")
    void deleteUser_validRequest_success() {
        String userId = "0ec45d4e-af2d-4e2d-a7e3-e26b73db3551";

        // Mock the repository's delete behavior, as it's the dependency of the service
        doNothing().when(userRepository).deleteById(userId);

        // Call the service method (no need to mock the service itself)
        userService.deleteUser(userId);

        // Verify that the repository's deleteById method was called with the correct argument
        verify(userRepository, times(1)).deleteById(userId);
    }
}
