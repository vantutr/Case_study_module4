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

    List<Product> findAllByOrderByIdDesc();

    @Query("SELECT p FROM Product p JOIN p.variants v GROUP BY p.id ORDER BY SUM(v.soldQuantity) DESC")
    List<Product> findBestsellerProducts(Pageable pageable);

    List<Product> findByCategory(Category category, Pageable pageable);

}
