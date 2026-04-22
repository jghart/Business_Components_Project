package com.ecommerce.glowshop.api;

import com.ecommerce.glowshop.api.dto.CategoryRequest;
import com.ecommerce.glowshop.api.dto.CategoryResponse;
import com.ecommerce.glowshop.config.OpenApiConfig;
import com.ecommerce.glowshop.model.Category;
import com.ecommerce.glowshop.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Category CRUD (admin writes require ADMIN role)")
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "List categories (paged)")
    public Page<CategoryResponse> list(@PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return categoryService.getCategoriesPage(pageable).map(CategoryResponse::from);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    public CategoryResponse get(@PathVariable Long id) {
        return CategoryResponse.from(categoryService.getCategoryById(id));
    }

    @PostMapping
    @Operation(summary = "Create category (ADMIN)")
    @SecurityRequirement(name = OpenApiConfig.JWT_SCHEME)
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        Category saved = categoryService.createCategory(request.name(), request.description());
        CategoryResponse body = CategoryResponse.from(saved);
        return ResponseEntity
                .created(URI.create("/api/categories/" + saved.getId()))
                .body(body);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category (ADMIN)")
    @SecurityRequirement(name = OpenApiConfig.JWT_SCHEME)
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category saved = categoryService.updateCategory(id, request.name(), request.description());
        return CategoryResponse.from(saved);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete category (ADMIN)")
    @SecurityRequirement(name = OpenApiConfig.JWT_SCHEME)
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
