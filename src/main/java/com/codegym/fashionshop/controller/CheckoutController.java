package com.codegym.fashionshop.controller;

import com.codegym.fashionshop.dto.CartDto;
import com.codegym.fashionshop.dto.CheckoutDto;
import com.codegym.fashionshop.model.User;
import com.codegym.fashionshop.service.IOrderService;
import com.codegym.fashionshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IUserService userService;

    @PostMapping
    public String placeOrder(@ModelAttribute CheckoutDto checkoutDto, HttpSession session) {
        CartDto cart = (CartDto) session.getAttribute("shoppingCart");
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart"; // Nếu giỏ hàng rỗng thì quay về
        }

        // Lấy thông tin người dùng đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (authentication != null && !"anonymousUser".equals(authentication.getName())) {
            currentUser = userService.findByUsername(authentication.getName());
        }

        try {
            orderService.placeOrder(cart, checkoutDto, currentUser);
            // Xóa giỏ hàng sau khi đặt hàng thành công
            session.removeAttribute("shoppingCart");
        } catch (Exception e) {
            // Xử lý lỗi (ví dụ: hết hàng) và quay lại giỏ hàng với thông báo
            e.printStackTrace();
            return "redirect:/cart?error=" + e.getMessage();
        }

        return "redirect:/checkout/order-success";  // Chuyển hướng đến trang đặt hàng thành công
    }

    @GetMapping("/order-success")
    public String orderSuccess() {
        return "order-success";
    }
}