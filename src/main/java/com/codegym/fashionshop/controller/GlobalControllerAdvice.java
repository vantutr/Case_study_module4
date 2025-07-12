package com.codegym.fashionshop.controller;

import com.codegym.fashionshop.dto.CartDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Thêm thuộc tính 'cartItemCount' vào model cho tất cả các request.
     * Thuộc tính này chứa tổng số lượng sản phẩm trong giỏ hàng.
     */
    @ModelAttribute("cartItemCount")
    public int getCartItemCount(HttpSession session) {
        CartDto cart = (CartDto) session.getAttribute("shoppingCart");
        if (cart == null) {
            return 0;
        }
        // Tính tổng số lượng của tất cả các sản phẩm trong giỏ
//        return cart.getItems().values().stream()
//                .mapToInt(item -> item.getQuantity())
//                .sum();

        return cart.getItems().size();
    }
}