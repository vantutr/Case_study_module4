// trong file: src/main/java/com/codegym/fashionshop/dto/CartItemDto.java
package com.codegym.fashionshop.dto;

import com.codegym.fashionshop.model.ProductVariant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CartItemDto {
    private Long variantId;
    private String productName;
    private String size;
    private BigDecimal price;
    private String imageUrl;
    private int quantity;

    // Constructor để chuyển đổi từ Entity sang DTO
    public CartItemDto(ProductVariant variant, int quantity) {
        this.variantId = variant.getId();
        this.productName = variant.getProduct().getName();
        this.size = variant.getSize();
        this.price = variant.getProduct().getPrice();
        this.imageUrl = variant.getProduct().getImageUrl();
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return this.price.multiply(new BigDecimal(this.quantity));
    }
}