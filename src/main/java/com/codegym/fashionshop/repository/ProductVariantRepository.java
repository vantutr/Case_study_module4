package com.codegym.fashionshop.repository;

import com.codegym.fashionshop.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    // Tìm một biến thể sản phẩm dựa vào productId và size
    Optional<ProductVariant> findByProduct_IdAndSizeIgnoreCase(Long productId, String size);
}