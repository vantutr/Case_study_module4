// Tạo file mới tại: src/main/java/com/codegym/fashionshop/controller/OrderHistoryController.java
package com.codegym.fashionshop.controller;

import com.codegym.fashionshop.configuration.CustomUserDetails;
import com.codegym.fashionshop.model.Order;
import com.codegym.fashionshop.model.User;
import com.codegym.fashionshop.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderHistoryController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/my-orders")
    public String showMyOrdersPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User currentUser = userDetails.getUser();
        List<Order> orders = orderService.findByUser(currentUser);

        model.addAttribute("orders", orders);
        return "my-orders";
    }

    @PostMapping("/my-orders/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes ra) {
        Order order = orderService.findById(id);
        User currentUser = userDetails.getUser();

        // Kiểm tra xem đơn hàng có thuộc về người dùng đang đăng nhập không và trạng thái có phải PENDING không
        if (order != null && order.getUser().getId().equals(currentUser.getId()) && "PENDING".equals(order.getStatus())) {
            orderService.updateStatus(id, "CANCELLED");
            ra.addFlashAttribute("message", "Đã hủy đơn hàng #" + id + " thành công.");
        } else {
            ra.addFlashAttribute("error_message", "Không thể hủy đơn hàng này.");
        }

        return "redirect:/my-orders";
    }
}