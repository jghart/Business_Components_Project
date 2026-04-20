package com.ecommerce.glowshop.service;

import com.ecommerce.glowshop.model.Category;
import com.ecommerce.glowshop.model.Product;
import com.ecommerce.glowshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProductById_notFound_throws() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> productService.getProductById(99L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void createProduct_persistsAndReturns() {
        Category cat = new Category(1L, "Skincare", "desc");
        when(categoryService.getCategoryById(1L)).thenReturn(cat);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(42L);
            return p;
        });

        Product saved = productService.createProduct("Serum", "d", null, null, null,
                new BigDecimal("29.99"), 5, 1L);

        assertEquals(42L, saved.getId());
        assertEquals("Serum", saved.getName());
        assertEquals(cat, saved.getCategory());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void decreaseStock_insufficient_throws() {
        Category cat = new Category(1L, "Skincare", null);
        Product p = new Product(1L, "X", null, null, null, null, new BigDecimal("10"), 1, cat);
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        assertThrows(IllegalArgumentException.class, () -> productService.decreaseStock(1L, 5));
        verify(productRepository, never()).save(any());
    }
}
