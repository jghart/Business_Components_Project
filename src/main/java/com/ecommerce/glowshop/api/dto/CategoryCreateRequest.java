package com.ecommerce.glowshop.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(
        @NotBlank String name,
        String description
) {}
