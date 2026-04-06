package com.ecommerce.glowshop.api.dto;

import jakarta.validation.constraints.NotBlank;

public record PlaceOrderRequest(
        @NotBlank String shippingAddress
) {}
