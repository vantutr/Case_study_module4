package com.codegym.fashionshop.service;

import com.codegym.fashionshop.dto.ProductVariantDto;

public interface IProductVariantService {

    void addStock(ProductVariantDto variantDto);

    void update(ProductVariantDto variantDto);

    Long deleteById(Long variantId);
}