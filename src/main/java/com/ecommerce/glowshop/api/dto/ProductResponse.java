package com.ecommerce.glowshop.api.dto;

import com.ecommerce.glowshop.model.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String ingredients,
        String skinType,
        String imageUrl,
        BigDecimal price,
        Integer stockQuantity,
        Long categoryId,
        String categoryName
) {
    public static ProductResponse fromEntity(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getIngredients(),
                p.getSkinType(),
                p.getImageUrl(),
                p.getPrice(),
                p.getStockQuantity(),
                p.getCategory().getId(),
                p.getCategory().getName()
        );
    }
}
