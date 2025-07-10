package com.codegym.fashionshop.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProductDto {
    private Long id;

    @NotEmpty(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá không được âm")
    private Double price;

    private String description;

    @NotNull(message = "Vui lòng chọn danh mục")
    private Long categoryId;

    private MultipartFile imageFile;

    private String imageUrl;
}