package com.ecommerce.glowshop.api.dto;

import com.ecommerce.glowshop.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryResponse(
        Long id,
        LocalDateTime createdAt,
        String status,
        BigDecimal totalAmount,
        String customerEmail
) {
    public static OrderSummaryResponse fromEntity(Order o, boolean includeCustomerEmail) {
        String email = includeCustomerEmail ? o.getUser().getEmail() : null;
        return new OrderSummaryResponse(
                o.getId(),
                o.getCreatedAt(),
                o.getStatus().name(),
                o.getTotalAmount(),
                email
        );
    }
}
