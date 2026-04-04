package com.ecommerce.glowshop.controller;

import com.ecommerce.glowshop.model.Cart;
import com.ecommerce.glowshop.service.CartService;
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
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String viewCart(Principal principal, Model model) {
        Cart cart = cartService.getOrCreateCart(principal.getName());
        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", cartService.getCartTotal(cart));
        model.addAttribute("itemCount", cartService.getCartItemCount(cart));
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        try {
            cartService.addToCart(principal.getName(), productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Item added to cart.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/update/{itemId}")
    public String updateQuantity(@PathVariable Long itemId,
                                 @RequestParam Integer quantity,
                                 RedirectAttributes redirectAttributes) {
        try {
            cartService.updateQuantity(itemId, quantity);
            redirectAttributes.addFlashAttribute("success", "Cart updated.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart/remove/{itemId}")
    public String removeItem(@PathVariable Long itemId,
                             RedirectAttributes redirectAttributes) {
        try {
            cartService.removeItem(itemId);
            redirectAttributes.addFlashAttribute("success", "Item removed from cart.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart/clear")
    public String clearCart(Principal principal, RedirectAttributes redirectAttributes) {
        cartService.clearCart(principal.getName());
        redirectAttributes.addFlashAttribute("success", "Cart cleared.");
        return "redirect:/cart";
    }
}
