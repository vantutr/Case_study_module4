package com.codegym.fashionshop.repository;

import com.codegym.fashionshop.model.Category;
import com.codegym.fashionshop.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.variants WHERE p.id = :id")
    Optional<Product> findByIdWithVariants(@Param("id") Long id);

    List<Product> findByNameContainingIgnoreCase(String name);

//    List<Product> findAllByOrderByIdDesc();

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.variants ORDER BY p.id DESC")
    List<Product> findNewestProductsWithVariants();

//    @Query("SELECT p FROM Product p JOIN p.variants v GROUP BY p.id ORDER BY SUM(v.soldQuantity) DESC")
//    List<Product> findBestsellerProducts(Pageable pageable);

    // 1. Lấy ra danh sách ID các sản phẩm bán chạy đã được sắp xếp
    @Query("SELECT p.id FROM Product p JOIN p.variants v GROUP BY p.id ORDER BY SUM(v.soldQuantity) DESC")
    List<Long> findBestsellerProductIds(Pageable pageable);

    // 2. Lấy thông tin đầy đủ của các sản phẩm từ danh sách ID
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.variants WHERE p.id IN :ids")
    List<Product> findByIdsWithVariants(@Param("ids") List<Long> ids);

    List<Product> findByCategory(Category category, Pageable pageable);

}
