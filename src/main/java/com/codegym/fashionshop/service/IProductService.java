package com.codegym.fashionshop.service;

import com.codegym.fashionshop.dto.ProductDto;
import com.codegym.fashionshop.model.Product;

import java.util.List;

public interface IProductService {

    List<Product> findAll();

    Product findById(Long id);

    void save(ProductDto productDto);

    void deleteById(Long id);

    long countTotalProducts();

    ProductDto findDtoById(Long id);

}
