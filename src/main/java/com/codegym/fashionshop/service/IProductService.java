package com.codegym.fashionshop.service;

import com.codegym.fashionshop.dto.ProductDto;
import com.codegym.fashionshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IProductService {

    List<Product> findAll();

    Product findById(Long id);

    void save(ProductDto productDto);

    void deleteById(Long id);

    long countTotalProducts();

    ProductDto findDtoById(Long id);

    Product findByIdWithVariants(Long id);

    List<Product> search(String keyword);

    List<Product> findNewestProducts();

    List<Product> findBestsellerProducts(int limit);

    List<Product> findRelatedProducts(Product product, int limit);

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

}
