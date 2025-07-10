package com.codegym.fashionshop.service;

import com.codegym.fashionshop.dto.CategoryDto;
import com.codegym.fashionshop.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> findAllSubCategories(); // Lấy danh sách các danh mục con
    List<Category> findAllParentCategories(); // Lấy danh sách các danh mục cha
    void save(CategoryDto categoryDto);
    CategoryDto findDtoById(Long id);
    void delete(Long id);
}
