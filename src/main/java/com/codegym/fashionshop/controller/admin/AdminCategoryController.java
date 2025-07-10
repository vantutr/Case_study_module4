package com.codegym.fashionshop.controller.admin;

import com.codegym.fashionshop.dto.CategoryDto;
import com.codegym.fashionshop.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private ICategoryService categoryService;

    // Hiển thị trang quản lý danh mục
    @GetMapping
    public String showCategoryPage(Model model) {
        // Thêm biến để highlight link trên sidebar
        model.addAttribute("active_link", "categories");

        // Lấy danh sách danh mục cha cho dropdown
        model.addAttribute("parentCategories", categoryService.findAllParentCategories());

        // Lấy danh sách danh mục con để hiển thị trong bảng
        model.addAttribute("subCategories", categoryService.findAllSubCategories());

        // Thêm DTO rỗng cho modal "Thêm mới", trừ khi có lỗi validation từ lần submit trước
        if (!model.containsAttribute("categoryDto")) {
            model.addAttribute("categoryDto", new CategoryDto());
        }

        return "admin/categories";
    }

    // Xử lý Thêm mới / Cập nhật
    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute("categoryDto") CategoryDto categoryDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Nếu có lỗi, gửi lại DTO và lỗi qua flash attributes để hiển thị
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryDto", bindingResult);
            redirectAttributes.addFlashAttribute("categoryDto", categoryDto);
            redirectAttributes.addFlashAttribute("error_message", "Dữ liệu không hợp lệ, vui lòng kiểm tra lại!");
            return "redirect:/admin/categories";
        }
        categoryService.save(categoryDto);
        redirectAttributes.addFlashAttribute("message", "Thao tác thành công!");
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}")
    @ResponseBody // Trả về dữ liệu dạng JSON thay vì một view
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto categoryDto = categoryService.findDtoById(id);
        if (categoryDto != null) {
            return ResponseEntity.ok(categoryDto);
        }
        return ResponseEntity.notFound().build();
    }

    // Xử lý xóa
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Đã xóa danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error_message", "Không thể xóa danh mục này vì có sản phẩm đang sử dụng.");
        }
        return "redirect:/admin/categories";
    }
}