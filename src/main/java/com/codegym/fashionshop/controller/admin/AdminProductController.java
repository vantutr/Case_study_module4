package com.codegym.fashionshop.controller.admin;

import com.codegym.fashionshop.dto.ProductDto;
import com.codegym.fashionshop.dto.ProductVariantDto;
import com.codegym.fashionshop.model.Product;
import com.codegym.fashionshop.service.ICategoryService;
import com.codegym.fashionshop.service.IProductService;
import com.codegym.fashionshop.service.IProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IProductVariantService variantService;

    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("active_link", "products");
        model.addAttribute("products", productService.findAll());
        model.addAttribute("categories", categoryService.findAllSubCategories());

        // Nếu không có DTO lỗi từ lần redirect trước, thì tạo DTO mới
        if (!model.containsAttribute("productDto")) {
            model.addAttribute("productDto", new ProductDto());
        }
        return "admin/products";
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("productDto") ProductDto productDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error_message", "Dữ liệu không hợp lệ, vui lòng kiểm tra lại!");
            // Gửi lại DTO và các lỗi để hiển thị trên form
            redirectAttributes.addFlashAttribute("productDto", productDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productDto", bindingResult);
            return "redirect:/admin/products";
        }

        // Nếu không có lỗi, tiến hành lưu
        productService.save(productDto);
        redirectAttributes.addFlashAttribute("message", "Thao tác thành công!");
        return "redirect:/admin/products";
    }

    // Endpoint cho AJAX để lấy thông tin sản phẩm khi sửa
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        // Bạn cần tạo phương thức findDtoById trong IProductService và implement nó
        ProductDto productDto = productService.findDtoById(id);
        return productDto != null ? ResponseEntity.ok(productDto) : ResponseEntity.notFound().build();
    }

    // Các phương thức khác...
    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) { return "redirect:/admin/products"; }
        model.addAttribute("active_link", "products");
        model.addAttribute("product", product);
        return "admin/product-detail";
    }

    @PostMapping("/variants/save")
    public String saveProductVariant(@ModelAttribute ProductVariantDto variantDto, RedirectAttributes redirectAttributes) {
        variantService.addStock(variantDto);
        redirectAttributes.addFlashAttribute("message", "Nhập kho thành công!");
        return "redirect:/admin/products/detail/" + variantDto.getProductId();
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error_message", "Không thể xóa sản phẩm này.");
        }
        return "redirect:/admin/products";
    }
}