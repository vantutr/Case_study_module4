// Tạo file mới tại: src/main/java/com/codegym/fashionshop/controller/CartController.java
package com.codegym.fashionshop.controller;

import com.codegym.fashionshop.dto.CartDto;
import com.codegym.fashionshop.model.ProductVariant;
import com.codegym.fashionshop.repository.ProductVariantRepository;
import com.codegym.fashionshop.service.impl.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductVariantRepository variantRepository;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long variantId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        ProductVariant variant = variantRepository.findByIdWithProduct(variantId).orElse(null);
        if (variant != null) {
            try {
                cartService.addToCart(variantId, variant, quantity, session);
                redirectAttributes.addFlashAttribute("message", "Đã thêm sản phẩm vào giỏ hàng!");
            } catch (RuntimeException e) {
                // Bắt lỗi và gửi thông báo về trang chi tiết sản phẩm
                redirectAttributes.addFlashAttribute("error_message", e.getMessage());
                return "redirect:/products/" + variant.getProduct().getId();
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Long variantId,
                             @RequestParam int quantity,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) { // Thêm RedirectAttributes
        try {
            cartService.updateCart(variantId, quantity, session);
        } catch (RuntimeException e) {
            // Bắt lỗi và gửi thông báo về lại trang giỏ hàng
            redirectAttributes.addFlashAttribute("error_message", e.getMessage());
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{variantId}")
    public String removeFromCart(@PathVariable Long variantId, HttpSession session) {
        cartService.removeFromCart(variantId, session);
        return "redirect:/cart";
    }
}