package com.codegym.fashionshop.controller.admin;

import com.codegym.fashionshop.service.IOrderService;
import com.codegym.fashionshop.service.IProductService;
import com.codegym.fashionshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {
        model.addAttribute("active_link", "dashboard");
        model.addAttribute("pendingOrdersCount", orderService.countPendingOrders());
        model.addAttribute("totalOrdersCount", orderService.countTotalOrders());
        model.addAttribute("totalProductsCount", productService.countTotalProducts());
        model.addAttribute("userAccountsCount", userService.countTotalUsers());
        return "admin/dashboard";
    }
}
