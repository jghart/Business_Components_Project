package com.ecommerce.glowshop.api.dto;

import com.ecommerce.glowshop.model.Category;

public record CategoryResponse(Long id, String name, String description) {
    public static CategoryResponse fromEntity(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getDescription());
    }
}
