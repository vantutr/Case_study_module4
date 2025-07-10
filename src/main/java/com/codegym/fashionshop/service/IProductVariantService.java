package com.codegym.fashionshop.service;

import com.codegym.fashionshop.dto.ProductVariantDto;

public interface IProductVariantService {
    /**
     * Nhập kho cho sản phẩm.
     * Nếu size đã tồn tại thì cộng dồn số lượng.
     * Nếu size chưa có thì tạo mới.
     * @param variantDto DTO chứa thông tin nhập kho
     */
    void addStock(ProductVariantDto variantDto);
}