package com.codegym.fashionshop.controller.admin;

import com.codegym.fashionshop.dto.UserAdminUpdateDto;
import com.codegym.fashionshop.model.Role;
import com.codegym.fashionshop.model.User;
import com.codegym.fashionshop.repository.RoleRepository;
import com.codegym.fashionshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public String showUserList(@RequestParam(required = false) String keyword, Model model) {
        List<User> users;
        if (StringUtils.hasText(keyword)) {
            users = userService.searchByUsername(keyword);
        } else {
            users = userService.findAll();
        }

        // Lấy danh sách quyền để đưa vào modal sửa
        List<Role> allRoles = roleRepository.findAll();

        model.addAttribute("active_link", "users");
        model.addAttribute("users", users);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("userDto", new UserAdminUpdateDto());
        model.addAttribute("keyword", keyword);
        return "admin/users";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("userDto") UserAdminUpdateDto userDto, RedirectAttributes redirectAttributes) {
        userService.updateUserFromAdmin(userDto);
        redirectAttributes.addFlashAttribute("message", "Cập nhật tài khoản thành công!");
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "Xóa tài khoản thành công!");
        return "redirect:/admin/users";
    }
}