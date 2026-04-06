package com.ecommerce.glowshop.service;

import com.ecommerce.glowshop.model.Product;
import com.ecommerce.glowshop.model.Review;
import com.ecommerce.glowshop.model.User;
import com.ecommerce.glowshop.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public List<Review> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    public Double getAverageRating(Long productId) {
        Double avg = reviewRepository.findAverageRatingByProductId(productId);
        return avg != null ? avg : 0.0;
    }

    public boolean hasUserReviewedProduct(String email, Long productId) {
        User user = userService.getUserByEmail(email);
        return reviewRepository.existsByUserIdAndProductId(user.getId(), productId);
    }

    @Transactional
    public void addReview(String email, Long productId, int rating, String comment) {
        User user = userService.getUserByEmail(email);
        if (user.getRole() != User.Role.CUSTOMER) {
            throw new IllegalArgumentException("Only customers can submit product reviews.");
        }
        if (reviewRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            throw new IllegalArgumentException("You have already reviewed this product.");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (comment == null || comment.isBlank()) {
            throw new IllegalArgumentException("Comment is required.");
        }
        Product product = productService.getProductById(productId);
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment.trim());
        reviewRepository.save(review);
    }
}
