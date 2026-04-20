package com.ecommerce.glowshop.repository;

import com.ecommerce.glowshop.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    /** Explicit query avoids parsing issues because our entity is named {@code Order}. */
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findAllOrdersNewestFirst();

    @EntityGraph(attributePaths = {"user"})
    @Query(value = "SELECT o FROM Order o ORDER BY o.createdAt DESC",
            countQuery = "SELECT count(o) FROM Order o")
    Page<Order> findAllOrdersNewestFirst(Pageable pageable);

    List<Order> findByStatus(Order.OrderStatus status);
}
