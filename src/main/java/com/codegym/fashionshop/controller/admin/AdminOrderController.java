package com.codegym.fashionshop.controller.admin;

import com.codegym.fashionshop.model.Order;
import com.codegym.fashionshop.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;


@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping
    public String showOrderList(@RequestParam(required = false) String status,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                Model model) {

        Specification<Order> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(status)) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // THAY ĐỔI LOGIC LỌC NGÀY Ở ĐÂY
            if (date != null) {
                // Tạo thời điểm bắt đầu của ngày (ví dụ: 2025-07-11 00:00:00)
                LocalDateTime startOfDay = date.atStartOfDay();
                // Tạo thời điểm kết thúc của ngày (ví dụ: 2025-07-11 23:59:59)
                LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
                // Tìm các đơn hàng có orderDate nằm giữa khoảng thời gian này
                predicates.add(criteriaBuilder.between(root.get("orderDate"), startOfDay, endOfDay));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        model.addAttribute("active_link", "orders");
        model.addAttribute("orders", orderService.findAll(spec));
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentDate", date);
        return "admin/orders";
    }

    @GetMapping("/detail/{id}")
    public String showOrderDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Order order = orderService.findByIdWithDetails(id);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error_message", "Không tìm thấy đơn hàng!");
            return "redirect:/admin/orders";
        }
        model.addAttribute("active_link", "orders");
        model.addAttribute("order", order);
        return "admin/order-detail";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId, @RequestParam String status, RedirectAttributes redirectAttributes) {
        orderService.updateStatus(orderId, status);
        redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái đơn hàng thành công!");
        return "redirect:/admin/orders/detail/" + orderId;
    }
}