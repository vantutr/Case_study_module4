package com.codegym.fashionshop.service;

import com.codegym.fashionshop.dto.CategoryDto;
import com.codegym.fashionshop.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> findAllSubCategories(); // Lấy danh sách các danh mục con
    // Phương thức cho trang Admin
    List<Category> findAllParentCategories();

    List<Category> findAllParentCategoriesWithChildren();
    void save(CategoryDto categoryDto);
    CategoryDto findDtoById(Long id);
    void delete(Long id);
}
