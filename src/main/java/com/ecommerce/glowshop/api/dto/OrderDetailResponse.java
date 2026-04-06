package com.ecommerce.glowshop.api.dto;

import com.ecommerce.glowshop.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponse(
        Long id,
        LocalDateTime createdAt,
        String status,
        BigDecimal totalAmount,
        String shippingAddress,
        String customerEmail,
        List<OrderLineResponse> lines
) {
    public static OrderDetailResponse fromEntity(Order o, boolean includeCustomerEmail) {
        String email = includeCustomerEmail ? o.getUser().getEmail() : null;
        List<OrderLineResponse> lines = o.getOrderItems().stream()
                .map(OrderLineResponse::fromEntity)
                .toList();
        return new OrderDetailResponse(
                o.getId(),
                o.getCreatedAt(),
                o.getStatus().name(),
                o.getTotalAmount(),
                o.getShippingAddress(),
                email,
                lines
        );
    }
}
