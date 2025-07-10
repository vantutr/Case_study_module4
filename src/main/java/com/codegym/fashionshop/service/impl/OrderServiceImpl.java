package com.codegym.fashionshop.service.impl;

import com.codegym.fashionshop.model.Order;
import com.codegym.fashionshop.repository.OrderRepository;
import com.codegym.fashionshop.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Phương thức findAll mới hỗ trợ Specification để lọc động
    @Override
    public List<Order> findAll(Specification<Order> spec) {
        return orderRepository.findAll(spec);
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public void updateStatus(Long orderId, String status) {
        Order order = findById(orderId);
        if (order != null && status != null && !status.isEmpty()) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }

    @Override
    public long countTotalOrders() {
        return orderRepository.count();
    }

    @Override
    public long countPendingOrders() {
        return orderRepository.countByStatus("PENDING");
    }
}