package com.ecommerce.glowshop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI glowshopOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("GlowShop REST API")
                        .description("Products and categories. GET endpoints are public; POST/PUT/DELETE require an ADMIN user (log in via the web app, then call the API from the same browser session).")
                        .version("1.0"));
    }
}
