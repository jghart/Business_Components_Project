package com.ecommerce.glowshop.service;

import com.ecommerce.glowshop.model.Category;
import com.ecommerce.glowshop.model.Product;
import com.ecommerce.glowshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findByStockQuantityLessThan(threshold);
    }

    public void createProduct(String name, String description, String ingredients,
                              String skinType, String imageUrl, BigDecimal price,
                              Integer stockQuantity, Long categoryId) {

        Category category = categoryService.getCategoryById(categoryId);

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setIngredients(ingredients);
        product.setSkinType(skinType);
        product.setImageUrl(imageUrl);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setCategory(category);

        productRepository.save(product);
    }

    public void updateProduct(Long id, String name, String description, String ingredients,
                              String skinType, String imageUrl, BigDecimal price,
                              Integer stockQuantity, Long categoryId) {

        Product product = getProductById(id);
        Category category = categoryService.getCategoryById(categoryId);

        product.setName(name);
        product.setDescription(description);
        product.setIngredients(ingredients);
        product.setSkinType(skinType);
        product.setImageUrl(imageUrl);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setCategory(category);

        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    public void updateStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        product.setStockQuantity(quantity);
        productRepository.save(product);
    }
}
