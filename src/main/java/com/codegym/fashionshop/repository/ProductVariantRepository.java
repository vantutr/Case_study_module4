package com.codegym.fashionshop.repository;

import com.codegym.fashionshop.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    // Tìm một biến thể sản phẩm dựa vào productId và size
    Optional<ProductVariant> findByProduct_IdAndSizeIgnoreCase(Long productId, String size);

    @Query("SELECT pv FROM ProductVariant pv JOIN FETCH pv.product p WHERE pv.id = :id")
    Optional<ProductVariant> findByIdWithProduct(@Param("id") Long id);
}