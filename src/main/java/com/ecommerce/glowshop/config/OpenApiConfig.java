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
                        .version("1.0")
                        .description("""
                                JSON endpoints for products, categories, and orders.
                                Public: GET catalog. Authenticated: place orders and view your orders.
                                Admin session: mutate catalog via POST/PUT/DELETE and manage orders under /api/admin/orders.
                                Use the same browser session as the MVC login (cookie), or a REST client that sends the session cookie.
                                """));
    }
}
