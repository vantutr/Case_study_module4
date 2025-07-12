package com.codegym.fashionshop.dto;

import lombok.Data;

@Data
public class UserAdminUpdateDto {
    private Long id;
    private Long roleId; // Chỉ cho phép chọn 1 role chính
    private boolean enabled;
}