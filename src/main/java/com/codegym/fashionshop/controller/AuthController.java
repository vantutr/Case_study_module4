package com.codegym.fashionshop.controller;

import com.codegym.fashionshop.dto.UserRegisterDto;
import com.codegym.fashionshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class AuthController {
    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserRegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("userDto") @Valid UserRegisterDto userDto, BindingResult result) {
        if (userService.findByUsername(userDto.getUsername()) != null) {
            result.rejectValue("username", null, "Tên đăng nhập đã tồn tại");
        }
        if (result.hasErrors()) {
            return "register";
        }
        userService.save(userDto);
        return "redirect:/register?success";
    }
}