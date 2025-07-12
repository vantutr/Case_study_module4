// Trong file: src/main/java/com/codegym/fashionshop/controller/HomeController.java
package com.codegym.fashionshop.controller;

import com.codegym.fashionshop.model.Product;
import com.codegym.fashionshop.service.ICategoryService;
import com.codegym.fashionshop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;

    // Chuyển hướng từ trang gốc sang /home
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showHomePage(Model model) {
        // Lấy danh mục cho menu
        model.addAttribute("parentCategories", categoryService.findAllParentCategoriesWithChildren());

        model.addAttribute("newestProducts", productService.findNewestProducts());

        model.addAttribute("bestsellerProducts", productService.findBestsellerProducts(8));

        return "home";
    }

    @GetMapping("/products/{id}")
    public String showProductDetailPage(@PathVariable Long id, Model model) {
        // Sử dụng phương thức đã tạo trước đó để lấy cả các biến thể
        Product product = productService.findByIdWithVariants(id);
        if (product == null) {
            return "redirect:/home"; // Nếu không tìm thấy sản phẩm, về trang chủ
        }

        List<Product> relatedProducts = productService.findRelatedProducts(product, 4);

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);

        // Thêm danh mục cho header
        model.addAttribute("parentCategories", categoryService.findAllParentCategoriesWithChildren());

        return "product-detail";
    }
}