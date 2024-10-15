package com.luongchivi.identity_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.luongchivi.identity_service.configuration.CustomJwtDecoder;
import com.luongchivi.identity_service.dto.request.user.UserCreationRequest;
import com.luongchivi.identity_service.dto.request.user.UserUpdateRequest;
import com.luongchivi.identity_service.dto.response.permission.PermissionResponse;
import com.luongchivi.identity_service.dto.response.role.RoleResponse;
import com.luongchivi.identity_service.dto.response.user.UserResponse;
import com.luongchivi.identity_service.entity.Permission;
import com.luongchivi.identity_service.entity.Role;
import com.luongchivi.identity_service.entity.User;
import com.luongchivi.identity_service.service.UserService;
import com.luongchivi.identity_service.share.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomJwtDecoder customJwtDecoder;

    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private RoleResponse roleResponse;
    private PermissionResponse permissionResponse;
    private LocalDate dateOfBirth;
    private Role role;
    private Permission permission;
    private User user;

    private Jwt tokenDecode;

    private final String TOKEN_BEARER = "valid.token.part";

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

        permission = Permission.builder()
                .name("read")
                .description("read permission description")
                .build();

        role = Role.builder()
                .name("User")
                .description("User role description")
                .permissions(Set.of(permission))
                .build();

        permissionResponse = PermissionResponse.builder()
                .name("read")
                .description("read permission description")
                .build();

        roleResponse = RoleResponse.builder()
                .name("User")
                .description("User role description")
                .permissions(Set.of(permissionResponse))
                .build();

        userResponse = UserResponse.builder()
                .id("e570ddff-76fc-4fb0-adf0-8979a57d3d76")
                .username("luongchivi060399")
                .firstName("Vi")
                .lastName("Luong Chi")
                .dateOfBirth(dateOfBirth)
                .roles(Set.of(roleResponse))
                .build();

        user = User.builder()
                .username("luongchivi060399")
                .firstName("Vi")
                .lastName("Luong Chi")
                .dateOfBirth(dateOfBirth)
                .roles(Set.of(role))
                .build();

        // Tạo một đối tượng Jwt hợp lệ
        tokenDecode = Jwt.withTokenValue(TOKEN_BEARER)
                .header("alg", "HS512")
                .subject(user.getUsername())
                .issuer("luongchivi.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS))
                .claim("scope", Utils.buildScope(user))
                .jti(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        when(userService.createUser(any())).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("results.id").value("e570ddff-76fc-4fb0-adf0-8979a57d3d76"));
    }

    @Test
    void createUser_usernameInvalid_failed() throws Exception {
        request.setUsername("Bar");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1005))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 8 characters"));
    }

    @Test
    void createUser_passwordInvalid_failed() throws Exception {
        request.setPassword("123");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1006))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Password must be at least 8 characters"));
    }

    @Test
    void getUserInfo_validRequest_success() throws Exception {
        // Mock phương thức decode để trả về Jwt hợp lệ
        when(customJwtDecoder.decode(anyString())).thenReturn(tokenDecode);

        // Mock service để trả về response mong muốn
        when(userService.getUserInfo()).thenReturn(userResponse);

        // Thực hiện request với header Authorization hợp lệ
        mockMvc.perform(MockMvcRequestBuilders.get("/users/info")
                        .header("Authorization", "Bearer " + TOKEN_BEARER)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("results.id").value("e570ddff-76fc-4fb0-adf0-8979a57d3d76"));
    }

    @Test
    void getUserInfo_AccessDenied_failed() throws Exception {
        when(userService.getUserInfo()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/info").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Unauthenticated"));
    }

    @Test
    void getUser_validRequest_success() throws Exception {
        // Mock phương thức decode để trả về Jwt hợp lệ
        when(customJwtDecoder.decode(anyString())).thenReturn(tokenDecode);

        // Mock service để trả về response mong muốn
        when(userService.getUser(anyString())).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/e570ddff-76fc-4fb0-adf0-8979a57d3d76")
                        .header("Authorization", "Bearer " + TOKEN_BEARER)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("results.id").value("e570ddff-76fc-4fb0-adf0-8979a57d3d76"))
                .andExpect(MockMvcResultMatchers.jsonPath("results.username").value("luongchivi060399"))
                .andExpect(MockMvcResultMatchers.jsonPath("results.firstName").value("Vi"))
                .andExpect(MockMvcResultMatchers.jsonPath("results.lastName").value("Luong Chi"))
                .andExpect(MockMvcResultMatchers.jsonPath("results.dateOfBirth").value(dateOfBirth.toString()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("results.roles[0].name").value("User"))
                .andExpect(MockMvcResultMatchers.jsonPath("results.roles[0].permissions[0].name")
                        .value("read"));
    }

    @Test
    void updateUser_validRequest_success() throws Exception {
        when(customJwtDecoder.decode(anyString())).thenReturn(tokenDecode);

        LocalDate dateOfBirth = LocalDate.now();

        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .firstName("Bar")
                .lastName("Foo")
                .dateOfBirth(dateOfBirth)
                .build();

        userResponse.setFirstName("Bar");
        userResponse.setLastName("Foo");
        userResponse.setDateOfBirth(dateOfBirth);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(updateRequest);

        when(userService.updateUser(anyString(), any())).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/e570ddff-76fc-4fb0-adf0-8979a57d3d76")
                        .header("Authorization", "Bearer " + TOKEN_BEARER)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("results.id").value("e570ddff-76fc-4fb0-adf0-8979a57d3d76"))
                .andExpect(MockMvcResultMatchers.jsonPath("results.username").value("luongchivi060399"))
                .andExpect(MockMvcResultMatchers.jsonPath("results.firstName").value("Bar"))
                .andExpect(MockMvcResultMatchers.jsonPath("results.lastName").value("Foo"))
                .andExpect(MockMvcResultMatchers.jsonPath("results.dateOfBirth").value(dateOfBirth.toString()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("results.roles[0].name").value("User"))
                .andExpect(MockMvcResultMatchers.jsonPath("results.roles[0].permissions[0].name")
                        .value("read"));
    }

    @Test
    void deleteUser_validRequest_success() throws Exception {
        when(customJwtDecoder.decode(anyString())).thenReturn(tokenDecode);

        doNothing().when(userService).deleteUser(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/0ec45d4e-af2d-4e2d-a7e3-e26b73db3551")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + TOKEN_BEARER))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Delete user successfully."));
    }

    @Test
    void deleteUser_AccessDenied_failed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/0ec45d4e-af2d-4e2d-a7e3-e26b73db3551")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1007))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Unauthenticated"));
    }
}
