package com.codegym.fashionshop.repository;

import com.codegym.fashionshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    /**
     * THÊM DÒNG NÀY VÀO
     * Spring Data JPA sẽ tự động tạo câu lệnh query "SELECT COUNT(*) FROM orders WHERE status = ?"
     * từ tên của phương thức này.
     * @param status Trạng thái cần đếm
     * @return số lượng đơn hàng
     */
    long countByStatus(String status);
}