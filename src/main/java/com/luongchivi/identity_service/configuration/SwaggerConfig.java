package com.luongchivi.identity_service.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info =
                @Info(
                        contact =
                                @Contact(
                                        name = "Luong Chi Vi",
                                        email = "chivi060399@gmail.com",
                                        url = "https://example.com"),
                        description = "API for Identity Service with OAuth2 and JWT security",
                        title = "OpenApi Identity Service",
                        version = "1.0",
                        license = @License(name = "MIT Licence", url = "https://example.com"),
                        termsOfService = "Terms of service"),
        servers = {
            @Server(description = "Development environment", url = "http://localhost:8080/identity"),
            @Server(description = "Production environment", url = "https://example.com/identity")
        }
        //        Apply all endpoint required token for using
        //        security = {
        //                @SecurityRequirement(
        //                        name = "bearerAuth"
        //                )
        //        }
        )
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {}
