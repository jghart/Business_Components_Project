package com.ecommerce.glowshop.service;

import com.ecommerce.glowshop.model.Cart;
import com.ecommerce.glowshop.model.CartItem;
import com.ecommerce.glowshop.model.Order;
import com.ecommerce.glowshop.model.OrderItem;
import com.ecommerce.glowshop.model.Product;
import com.ecommerce.glowshop.model.User;
import com.ecommerce.glowshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    
    @Transactional
    public Order placeOrder(String email, String shippingAddress) {
        if (shippingAddress == null || shippingAddress.isBlank()) {
            throw new IllegalArgumentException("Shipping address is required.");
        }

        User user = userService.getUserByEmail(email);
        Cart cart = cartService.getOrCreateCart(email);
        List<CartItem> lines = new ArrayList<>(cart.getItems());
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Your cart is empty.");
        }

        for (CartItem line : lines) {
            Product product = productService.getProductById(line.getProduct().getId());
            if (product.getStockQuantity() < line.getQuantity()) {
                throw new IllegalArgumentException(
                        "Not enough stock for \"" + product.getName() + "\". Available: "
                                + product.getStockQuantity());
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress.trim());
        order.setStatus(Order.OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem line : lines) {
            Product product = productService.getProductById(line.getProduct().getId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(line.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            order.getOrderItems().add(orderItem);
            total = total.add(orderItem.getSubtotal());
            productService.decreaseStock(product.getId(), line.getQuantity());
        }

        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);
        cartService.clearCart(email);
        return saved;
    }

    public List<Order> getOrdersForCustomer(String email) {
        User user = userService.getUserByEmail(email);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public Order getOrderForCustomer(Long orderId, String email) {
        User user = userService.getUserByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found."));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have access to this order.");
        }
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllOrdersNewestFirst();
    }

    public Page<Order> getAllOrdersPage(Pageable pageable) {
        return orderRepository.findAllOrdersNewestFirst(pageable);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found."));
    }

    @Transactional
    public void updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }
}
