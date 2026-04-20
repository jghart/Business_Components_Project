package com.ecommerce.glowshop.service;

import com.ecommerce.glowshop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void placeOrder_blankShipping_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> orderService.placeOrder("user@test.com", "   "));
    }

    @Test
    void placeOrder_nullShipping_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> orderService.placeOrder("user@test.com", null));
    }
}
