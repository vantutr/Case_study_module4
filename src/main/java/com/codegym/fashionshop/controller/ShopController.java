package com.codegym.fashionshop.controller;

import com.codegym.fashionshop.model.Product;
import com.codegym.fashionshop.service.ICategoryService;
import com.codegym.fashionshop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public String showShopPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "newest") String sort,
            Model model) {

        // 1. Xử lý Sắp xếp (Sort) và Phân trang (Pageable)
        Sort sorting = Sort.by("id").descending(); // Mặc định: mới nhất
        if ("oldest".equals(sort)) {
            sorting = Sort.by("id").ascending();
        } else if ("price-asc".equals(sort)) {
            sorting = Sort.by("price").ascending();
        } else if ("price-desc".equals(sort)) {
            sorting = Sort.by("price").descending();
        }
        Pageable pageable = PageRequest.of(page, size, sorting);

        // 2. Xử lý Lọc (Filter) bằng Specification
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(keyword)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
            }
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // 3. Gọi service để lấy dữ liệu phân trang
        // Cần tạo phương thức findAll(spec, pageable) trong ProductService
        Page<Product> productPage = productService.findAll(spec, pageable);

        // 4. Gửi dữ liệu ra View
        model.addAttribute("productPage", productPage);
        model.addAttribute("parentCategories", categoryService.findAllParentCategoriesWithChildren());

        // Gửi lại các tham số lọc để giữ trạng thái trên form
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);

        return "shop";
    }
}