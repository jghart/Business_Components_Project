package com.ecommerce.glowshop.controller;

import com.ecommerce.glowshop.model.Product;
import com.ecommerce.glowshop.service.CategoryService;
import com.ecommerce.glowshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) String search,
                               @RequestParam(required = false) Long categoryId,
                               @PageableDefault(size = 12, sort = "name", direction = Sort.Direction.ASC)
                               Pageable pageable,
                               Model model) {
        Page<Product> page;
        if (search != null && !search.trim().isEmpty()) {
            page = productService.getProductsPageBySearch(search.trim(), pageable);
            model.addAttribute("search", search);
        } else if (categoryId != null) {
            page = productService.getProductsPageByCategory(categoryId, pageable);
            model.addAttribute("selectedCategoryId", categoryId);
        } else {
            page = productService.getProductsPage(pageable);
        }

        model.addAttribute("products", page);
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
