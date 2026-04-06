package com.ecommerce.glowshop.controller;

import com.ecommerce.glowshop.model.Product;
import com.ecommerce.glowshop.service.CategoryService;
import com.ecommerce.glowshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) String search,
                               @RequestParam(required = false) Long categoryId,
                               Model model) {
        List<Product> products;

        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search.trim());
            model.addAttribute("search", search);
        } else if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId);
            model.addAttribute("selectedCategoryId", categoryId);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-detail";
    }
}
