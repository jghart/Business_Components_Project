package com.ecommerce.glowshop.controller;

import com.ecommerce.glowshop.model.Cart;
import com.ecommerce.glowshop.model.Order;
import com.ecommerce.glowshop.service.CartService;
import com.ecommerce.glowshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String checkoutForm(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        Cart cart = cartService.getOrCreateCart(principal.getName());
        if (cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty. Add products before checkout.");
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", cartService.getCartTotal(cart));
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String shippingAddress,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.placeOrder(principal.getName(), shippingAddress);
            redirectAttributes.addFlashAttribute("success", "Thank you! Your order was placed.");
            return "redirect:/orders/" + order.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/orders")
    public String myOrders(Principal principal, Model model) {
        model.addAttribute("orders", orderService.getOrdersForCustomer(principal.getName()));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id,
                              Principal principal,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.getOrderForCustomer(id, principal.getName());
            model.addAttribute("order", order);
            return "order-detail";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders";
        }
    }
}
