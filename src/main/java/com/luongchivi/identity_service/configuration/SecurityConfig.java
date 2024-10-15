package com.luongchivi.identity_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfig {
    final String[] PUBLIC_POST_ENDPOINTS = {
        "/users", "/auth/login", "/auth/introspect", "/auth/logout", "/auth/refresh-token", "/posts"
    };

    final String[] PUBLIC_GET_ENDPOINTS = {"/tags", "/categories"};

    final String[] SWAGGER_WHITELIST = {
        "/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**"
    };

    CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                // Cho phép truy cập không cần xác thực vào các đường dẫn trong SWAGGER_WHITELIST
                .requestMatchers(SWAGGER_WHITELIST)
                .permitAll()
                // Cho phép POST vào các PUBLIC_ENDPOINTS mà không cần xác thực
                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS)
                .permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS)
                .permitAll()
                // GET /users yêu cầu ROLE_Admin hoặc quyền "read"
                .requestMatchers(HttpMethod.GET, "/users")
                .hasAnyAuthority("ROLE_Admin", "read")
                .requestMatchers(HttpMethod.DELETE, "/users")
                .hasAuthority("ROLE_Admin")
                // POST vào /tags chỉ dành cho ROLE_Admin
                .requestMatchers(HttpMethod.POST, "/tags")
                .hasAuthority("ROLE_Admin")
                // Tất cả các phương thức đến /roles yêu cầu ROLE_Admin
                .requestMatchers(HttpMethod.DELETE, "/tags/")
                .hasAuthority("ROLE_Admin")
                .requestMatchers(HttpMethod.POST, "/categories")
                .hasAuthority("ROLE_Admin")
                // Tất cả các phương thức đến /roles yêu cầu ROLE_Admin
                .requestMatchers(HttpMethod.DELETE, "/categories/")
                .hasAuthority("ROLE_Admin")
                .requestMatchers("*", "/roles")
                .hasAuthority("ROLE_Admin")
                // Tất cả các phương thức đến /permissions yêu cầu ROLE_Admin
                .requestMatchers("*", "/permissions")
                .hasAuthority("ROLE_Admin")
                // Tất cả các request khác đều yêu cầu ROLE_Admin
                .anyRequest()
                .authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationFailureHandler())
                .accessDeniedHandler(new JwtAccessDeniedHandler()));

        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
