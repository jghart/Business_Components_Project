package com.ecommerce.glowshop;

import com.ecommerce.glowshop.model.Category;
import com.ecommerce.glowshop.model.User;
import com.ecommerce.glowshop.repository.CategoryRepository;
import com.ecommerce.glowshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createAdminIfNotExists();
        createCategoriesIfNotExist();
    }

    private void createAdminIfNotExists() {
        if (!userRepository.existsByEmail("admin@glowshop.com")) {
            User admin = new User();
            admin.setName("GlowShop Admin");
            admin.setEmail("admin@glowshop.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println(">>> Admin account created: admin@glowshop.com / admin123");
        }
    }

    private void createCategoriesIfNotExist() {
        List<String[]> categories = List.of(
                new String[]{"Skincare", "Cleansers, moisturizers, serums, and treatments for all skin types"},
                new String[]{"Haircare", "Shampoos, conditioners, oils, and styling products for healthy hair"},
                new String[]{"Wellness", "Supplements, aromatherapy, and holistic health products"},
                new String[]{"Fragrance", "Perfumes, body mists, and scented products"}
        );

        for (String[] cat : categories) {
            if (!categoryRepository.existsByName(cat[0])) {
                Category category = new Category();
                category.setName(cat[0]);
                category.setDescription(cat[1]);
                categoryRepository.save(category);
                System.out.println(">>> Category created: " + cat[0]);
            }
        }
    }
}
