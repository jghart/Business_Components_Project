package com.ecommerce.glowshop.api;

import com.ecommerce.glowshop.api.dto.OrderDetailResponse;
import com.ecommerce.glowshop.api.dto.OrderSummaryResponse;
import com.ecommerce.glowshop.api.dto.PlaceOrderRequest;
import com.ecommerce.glowshop.model.Order;
import com.ecommerce.glowshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

/**
 * Customer-facing order API. Admins can read any order by id; customers only their own.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/me")
    public List<OrderSummaryResponse> myOrders(Principal principal) {
        return orderService.getOrdersForCustomer(principal.getName()).stream()
                .map(o -> OrderSummaryResponse.fromEntity(o, false))
                .toList();
    }

    /**
     * Path variable is restricted to digits so it does not capture {@code /me}.
     */
    @GetMapping("/{id:\\d+}")
    public OrderDetailResponse getOrder(@PathVariable Long id,
                                        Principal principal,
                                        Authentication authentication) {
        boolean admin = isAdmin(authentication);
        Order order = admin
                ? orderService.getOrderById(id)
                : orderService.getOrderForCustomer(id, principal.getName());
        return OrderDetailResponse.fromEntity(order, admin);
    }

    @PostMapping
    public ResponseEntity<OrderDetailResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest body,
                                                          Principal principal) {
        Order order = orderService.placeOrder(principal.getName(), body.shippingAddress());
        return ResponseEntity
                .created(URI.create("/api/orders/" + order.getId()))
                .body(OrderDetailResponse.fromEntity(order, false));
    }

    private static boolean isAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }
}
