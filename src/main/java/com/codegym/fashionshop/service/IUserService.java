package com.codegym.fashionshop.service;

import com.codegym.fashionshop.dto.UserAdminUpdateDto;
import com.codegym.fashionshop.dto.UserRegisterDto;
import com.codegym.fashionshop.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {
    User findByUsername(String name);
    void save(UserRegisterDto userRegisterDto);

    long countTotalUsers();

    List<User> findAll();

    List<User> searchByUsername(String username);

    User findById(Long id);

    void updateUserFromAdmin(UserAdminUpdateDto dto);

    void deleteUser(Long id);
}