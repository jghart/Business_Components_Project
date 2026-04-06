package com.ecommerce.glowshop.api;

import com.ecommerce.glowshop.api.dto.ProductCreateRequest;
import com.ecommerce.glowshop.api.dto.ProductResponse;
import com.ecommerce.glowshop.model.Product;
import com.ecommerce.glowshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * JSON API for the product catalog.
 * <p>
 * <strong>Teaching note:</strong> We return DTOs ({@link ProductResponse}) instead of JPA entities so Jackson
 * does not accidentally serialize lazy associations or create infinite graphs. The service layer stays unchanged.
 */
@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductResponse> listAll() {
        return productService.getAllProducts().stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return ProductResponse.fromEntity(productService.getProductById(id));
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductResponse> byCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId).stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @GetMapping("/search")
    public List<ProductResponse> search(@RequestParam("q") String q) {
        return productService.searchProducts(q).stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest body) {
        Product created = productService.createProduct(
                body.name(),
                body.description(),
                body.ingredients(),
                body.skinType(),
                body.imageUrl(),
                body.price(),
                body.stockQuantity(),
                body.categoryId()
        );
        return ResponseEntity.created(URI.create("/api/products/" + created.getId()))
                .body(ProductResponse.fromEntity(created));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductCreateRequest body) {
        productService.updateProduct(
                id,
                body.name(),
                body.description(),
                body.ingredients(),
                body.skinType(),
                body.imageUrl(),
                body.price(),
                body.stockQuantity(),
                body.categoryId()
        );
        return ProductResponse.fromEntity(productService.getProductById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
