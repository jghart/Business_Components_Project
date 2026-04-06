package com.ecommerce.glowshop.api;

import com.ecommerce.glowshop.api.dto.OrderDetailResponse;
import com.ecommerce.glowshop.api.dto.OrderStatusUpdateRequest;
import com.ecommerce.glowshop.api.dto.OrderSummaryResponse;
import com.ecommerce.glowshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderRestController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderSummaryResponse> listAll() {
        return orderService.getAllOrders().stream()
                .map(o -> OrderSummaryResponse.fromEntity(o, true))
                .toList();
    }

    @GetMapping("/{id}")
    public OrderDetailResponse getById(@PathVariable Long id) {
        return OrderDetailResponse.fromEntity(orderService.getOrderById(id), true);
    }

    @PatchMapping("/{id}/status")
    public OrderDetailResponse updateStatus(@PathVariable Long id,
                                          @Valid @RequestBody OrderStatusUpdateRequest body) {
        orderService.updateOrderStatus(id, body.status());
        return OrderDetailResponse.fromEntity(orderService.getOrderById(id), true);
    }
}
