package com.codegym.fashionshop.service.impl;

import com.codegym.fashionshop.dto.CategoryDto;
import com.codegym.fashionshop.model.Category;
import com.codegym.fashionshop.repository.CategoryRepository;
import com.codegym.fashionshop.service.ICategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAllSubCategories() {
        return categoryRepository.findByParentIsNotNull();
    }

    // Triển khai phương thức cho Admin
    @Override
    public List<Category> findAllParentCategories() {
        return categoryRepository.findByParentIsNull();
    }

    @Override
    public List<Category> findAllParentCategoriesWithChildren() {
        return categoryRepository.findAllParentCategoriesWithChildren();
    }

    // Cập nhật logic để xử lý parentId
    @Override
    public void save(CategoryDto categoryDto) {
        Category category = new Category();

        // Tìm thực thể cha từ DB dựa vào parentId
        Category parent = categoryRepository.findById(categoryDto.getParentId()).orElse(null);

        BeanUtils.copyProperties(categoryDto, category);
        category.setParent(parent); // Gán thực thể cha cho danh mục đang lưu

        categoryRepository.save(category);
    }

    @Override
    public CategoryDto findDtoById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category, categoryDto);
            // Gán parentId cho DTO để hiển thị trên form edit
            if (category.getParent() != null) {
                categoryDto.setParentId(category.getParent().getId());
            }
            return categoryDto;
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
