package com.codegym.fashionshop.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CategoryDto {
    private Long id;

    @NotEmpty(message = "Tên danh mục không được để trống.")
    private String name;

    private String description;

    // Thêm trường này để nhận ID của danh mục cha từ form
    @NotNull(message = "Vui lòng chọn danh mục cha.")
    private Long parentId;
}