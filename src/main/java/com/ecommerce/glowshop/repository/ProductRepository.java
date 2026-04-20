package com.ecommerce.glowshop.repository;

import com.ecommerce.glowshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    @EntityGraph(attributePaths = {"category"})
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    List<Product> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"category"})
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Product> findByStockQuantityLessThan(Integer threshold);

    @EntityGraph(attributePaths = {"category"})
    @Override
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"category"})
    @Override
    Optional<Product> findById(Long id);
}
