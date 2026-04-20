package com.ecommerce.glowshop.api;

import com.ecommerce.glowshop.api.dto.ProductRequest;
import com.ecommerce.glowshop.api.dto.ProductResponse;
import com.ecommerce.glowshop.model.Product;
import com.ecommerce.glowshop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Catalog CRUD (admin writes require ADMIN role)")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "List products (paged, optional search or category filter)")
    public Page<ProductResponse> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 12, sort = "name") Pageable pageable) {
        Page<Product> page;
        if (search != null && !search.isBlank()) {
            page = productService.getProductsPageBySearch(search.trim(), pageable);
        } else if (categoryId != null) {
            page = productService.getProductsPageByCategory(categoryId, pageable);
        } else {
            page = productService.getProductsPage(pageable);
        }
        return page.map(ProductResponse::from);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id")
    public ProductResponse get(@PathVariable Long id) {
        return ProductResponse.from(productService.getProductById(id));
    }

    @PostMapping
    @Operation(summary = "Create product (ADMIN)")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        Product saved = productService.createProduct(
                request.name(),
                request.description(),
                request.ingredients(),
                request.skinType(),
                request.imageUrl(),
                request.price(),
                request.stockQuantity(),
                request.categoryId());
        ProductResponse body = ProductResponse.from(saved);
        return ResponseEntity
                .created(URI.create("/api/products/" + saved.getId()))
                .body(body);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product (ADMIN)")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        Product saved = productService.updateProduct(
                id,
                request.name(),
                request.description(),
                request.ingredients(),
                request.skinType(),
                request.imageUrl(),
                request.price(),
                request.stockQuantity(),
                request.categoryId());
        return ProductResponse.from(saved);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete product (ADMIN)")
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
