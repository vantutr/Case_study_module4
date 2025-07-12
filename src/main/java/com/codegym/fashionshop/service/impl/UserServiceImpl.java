package com.codegym.fashionshop.service.impl;

import com.codegym.fashionshop.configuration.CustomUserDetails;
import com.codegym.fashionshop.dto.UserAdminUpdateDto;
import com.codegym.fashionshop.dto.UserRegisterDto;
import com.codegym.fashionshop.model.Role;
import com.codegym.fashionshop.model.User;
import com.codegym.fashionshop.repository.RoleRepository;
import com.codegym.fashionshop.repository.UserRepository;
import com.codegym.fashionshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public void save(UserRegisterDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setEnabled(true);
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public long countTotalUsers() {
        return userRepository.count();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        // kiểm tra user có tồn tại hay không.
        // Nếu không tìm thấy, ném ra exception này.
        if (user == null) {
            throw new UsernameNotFoundException("Tên đăng nhập hoặc mật khẩu không hợp lệ.");
        }

        // Nếu tìm thấy, luôn trả về CustomUserDetails.
        // Spring Security sẽ tự kiểm tra thuộc tính isEnabled() bên trong nó.
        return new CustomUserDetails(user);
    }

    @Override
    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> searchByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void updateUserFromAdmin(UserAdminUpdateDto dto) {
        User user = findById(dto.getId());
        if (user != null) {
            Role role = roleRepository.findById(dto.getRoleId()).orElse(null);
            if (role != null) {
                user.setRoles(Collections.singleton(role));
            }
            user.setEnabled(dto.isEnabled());
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Long id) {
        // Ngăn admin tự xóa chính mình
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User userToDelete = findById(id);
        if (userToDelete != null && !userToDelete.getUsername().equals(currentUsername)) {
            userRepository.deleteById(id);
        }
    }
}