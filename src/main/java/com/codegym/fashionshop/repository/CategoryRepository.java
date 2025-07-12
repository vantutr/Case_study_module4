package com.codegym.fashionshop.repository;

import com.codegym.fashionshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Phương thức này dùng cho trang Admin
    List<Category> findByParentIsNull();

    // Tìm tất cả các danh mục có parent_id là NULL
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL")
    List<Category> findAllParentCategoriesWithChildren();

    // Tìm tất cả các danh mục có parent_id không phải là NULL (đây là các danh mục con)
    List<Category> findByParentIsNotNull();
}
