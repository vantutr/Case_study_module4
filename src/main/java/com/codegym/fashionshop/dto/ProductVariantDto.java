package com.codegym.fashionshop.dto;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProductVariantDto {
    @NotNull
    private Long productId;

    @NotEmpty(message = "Size không được để trống.")
    private String size;

    @NotNull(message = "Số lượng không được để trống.")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0.")
    private Integer quantity;
}