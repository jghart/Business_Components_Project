package com.ecommerce.glowshop.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String JWT_SCHEME = "bearer-jwt";

    @Bean
    public OpenAPI glowshopOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("GlowShop REST API")
                        .description("""
                                Products and categories. GET endpoints are public.

                                **Admin writes** (POST/PUT/DELETE): use either (1) web login + same-browser session, or (2) JWT: \
                                `POST /api/auth/login` with JSON `{"email","password"}`, then send \
                                `Authorization: Bearer <accessToken>` on API requests. \
                                Click **Authorize** and enter `Bearer <token>` (or paste the raw token depending on Swagger UI version).""")
                        .version("1.0"))
                .components(new Components()
                        .addSecuritySchemes(JWT_SCHEME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
