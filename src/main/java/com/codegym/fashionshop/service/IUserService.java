package com.codegym.fashionshop.service;

import com.codegym.fashionshop.dto.UserRegisterDto;
import com.codegym.fashionshop.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    User findByUsername(String name);
    void save(UserRegisterDto userRegisterDto);

    long countTotalUsers();
}