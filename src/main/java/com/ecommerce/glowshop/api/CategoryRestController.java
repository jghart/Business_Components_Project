package com.ecommerce.glowshop.api;

import com.ecommerce.glowshop.api.dto.CategoryCreateRequest;
import com.ecommerce.glowshop.api.dto.CategoryResponse;
import com.ecommerce.glowshop.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> listAll() {
        return categoryService.getAllCategories().stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id) {
        return CategoryResponse.fromEntity(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest body) {
        var created = categoryService.createCategory(body.name(), body.description());
        return ResponseEntity.created(URI.create("/api/categories/" + created.getId()))
                .body(CategoryResponse.fromEntity(created));
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryCreateRequest body) {
        categoryService.updateCategory(id, body.name(), body.description());
        return CategoryResponse.fromEntity(categoryService.getCategoryById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
