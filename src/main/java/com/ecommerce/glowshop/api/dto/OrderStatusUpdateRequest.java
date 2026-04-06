package com.ecommerce.glowshop.api.dto;

import com.ecommerce.glowshop.model.Order;
import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateRequest(
        @NotNull Order.OrderStatus status
) {}
