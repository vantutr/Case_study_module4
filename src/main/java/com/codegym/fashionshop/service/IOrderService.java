package com.codegym.fashionshop.service;

import com.codegym.fashionshop.model.Order;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IOrderService {
    /**
     * Lấy danh sách đơn hàng có hỗ trợ lọc.
     * @param spec Specification chứa các điều kiện lọc
     * @return danh sách đơn hàng
     */
    List<Order> findAll(Specification<Order> spec);

    /**
     * Tìm đơn hàng theo ID.
     * @param id ID của đơn hàng
     * @return đối tượng Order hoặc null
     */
    Order findById(Long id);

    /**
     * Cập nhật trạng thái cho một đơn hàng.
     * @param orderId ID của đơn hàng
     * @param status Trạng thái mới
     */
    void updateStatus(Long orderId, String status);

    // Các phương thức đếm cho dashboard
    long countTotalOrders();
    long countPendingOrders();
}