package com.codegym.fashionshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {
        // 1. Dữ liệu cho các card
        model.addAttribute("pendingOrdersCount", 42); // Lấy từ orderService
        model.addAttribute("totalOrdersCount", 250); // Lấy từ orderService
        model.addAttribute("userAccountsCount", 185); // Lấy từ userService
        model.addAttribute("pendingCommentsCount", 15); // Lấy từ commentService

        // 2. Dữ liệu cho biểu đồ
        List<String> chartLabels = Arrays.asList("Ngày 1", "Ngày 2", "Ngày 3", "Ngày 4", "Ngày 5", "Ngày 6", "Ngày 7");
        List<Integer> chartData = Arrays.asList(12, 19, 3, 5, 8, 3, 10);
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        // 3. Dữ liệu cho Top 5 danh mục (cần tạo DTO hoặc class tạm)
        // List<CategorySaleDTO> topCategories = categoryService.findTop5SellingCategories();
        // model.addAttribute("topCategories", topCategories);

        return "admin/dashboard";
    }
}
