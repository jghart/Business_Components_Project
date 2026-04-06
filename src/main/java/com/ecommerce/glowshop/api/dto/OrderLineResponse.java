package com.ecommerce.glowshop.api.dto;

import com.ecommerce.glowshop.model.OrderItem;

import java.math.BigDecimal;

public record OrderLineResponse(
        Long productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
    public static OrderLineResponse fromEntity(OrderItem line) {
        return new OrderLineResponse(
                line.getProduct().getId(),
                line.getProduct().getName(),
                line.getQuantity(),
                line.getPriceAtPurchase(),
                line.getSubtotal()
        );
    }
}
