package com.ecommerce.glowshop.service;

import com.ecommerce.glowshop.model.Category;
import com.ecommerce.glowshop.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getCategoryById_notFound_throws() {
        when(categoryRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryById(5L));
    }

    @Test
    void createCategory_duplicateName_throws() {
        when(categoryRepository.existsByName("Dup")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.createCategory("Dup", "desc"));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void createCategory_savesAndReturns() {
        when(categoryRepository.existsByName("New")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> {
            Category c = inv.getArgument(0);
            c.setId(7L);
            return c;
        });

        Category saved = categoryService.createCategory("New", "Description");

        assertEquals(7L, saved.getId());
        assertEquals("New", saved.getName());
        verify(categoryRepository).save(any(Category.class));
    }
}
