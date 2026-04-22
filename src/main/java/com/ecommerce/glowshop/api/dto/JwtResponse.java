package com.ecommerce.glowshop.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtResponse(
        @JsonProperty("accessToken")
        String accessToken,
        @JsonProperty("tokenType")
        String tokenType,
        @JsonProperty("expiresIn")
        long expiresInMillis
) {
}
