package com.luongchivi.identity_service.configuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.luongchivi.identity_service.entity.Permission;
import com.luongchivi.identity_service.entity.Role;
import com.luongchivi.identity_service.entity.User;
import com.luongchivi.identity_service.repository.PermissionRepository;
import com.luongchivi.identity_service.repository.RoleRepository;
import com.luongchivi.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor()
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.datasource",
            value = "driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner() {
        return args -> {
            // Ensure required permissions exist
            Permission permissionRead = permissionRepository
                    .findById("read")
                    .orElseGet(() -> permissionRepository.save(new Permission("read", "Read permission")));
            Permission permissionWrite = permissionRepository
                    .findById("write")
                    .orElseGet(() -> permissionRepository.save(new Permission("write", "Write permission")));
            Permission permissionUpdate = permissionRepository
                    .findById("update")
                    .orElseGet(() -> permissionRepository.save(new Permission("update", "Update permission")));
            Permission permissionDelete = permissionRepository
                    .findById("delete")
                    .orElseGet(() -> permissionRepository.save(new Permission("delete", "Delete permission")));

            // Ensure Admin role exists and is assigned permissions
            Role adminRole = roleRepository.findById("Admin").orElseGet(() -> {
                Role newAdminRole = new Role();
                newAdminRole.setName("Admin");
                newAdminRole.setDescription("Admin role description");
                newAdminRole.setPermissions(
                        new HashSet<>(Set.of(permissionRead, permissionWrite, permissionUpdate, permissionDelete)));
                return roleRepository.save(newAdminRole);
            });

            // Ensure User role exists with basic permissions
            Role userRole = roleRepository.findById("User").orElseGet(() -> {
                Role newUserRole = new Role();
                newUserRole.setName("User");
                newUserRole.setDescription("User role description");
                newUserRole.setPermissions(new HashSet<>(Set.of(permissionRead)));
                return roleRepository.save(newUserRole);
            });

            // Create default admin user if not present
            if (userRepository.findByUsername("Admin").isEmpty()) {
                User adminUser = User.builder()
                        .username("Admin")
                        .password(passwordEncoder.encode("Admin"))
                        .roles(Set.of(adminRole, userRole)) // Assign the admin role
                        .build();

                userRepository.save(adminUser);
                log.warn("Admin user has been created with default credentials: admin/admin");
            }
        };
    }
}
