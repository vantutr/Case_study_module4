package com.codegym.fashionshop.repository;

import com.codegym.fashionshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Tìm tất cả các danh mục có parent_id là NULL (đây là các danh mục gốc)
    List<Category> findByParentIsNull();

    // Tìm tất cả các danh mục có parent_id không phải là NULL (đây là các danh mục con)
    List<Category> findByParentIsNotNull();
}
