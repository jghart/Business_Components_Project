package com.ecommerce.glowshop.controller;

import com.ecommerce.glowshop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/products/{productId}/reviews")
    public String addReview(@PathVariable Long productId,
                            @RequestParam int rating,
                            @RequestParam String comment,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        try {
            reviewService.addReview(principal.getName(), productId, rating, comment);
            redirectAttributes.addFlashAttribute("success", "Thanks — your review was posted.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("reviewError", e.getMessage());
        }
        return "redirect:/products/" + productId;
    }
}
