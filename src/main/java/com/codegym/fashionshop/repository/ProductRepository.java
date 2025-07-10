package com.codegym.fashionshop.repository;

import com.codegym.fashionshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
