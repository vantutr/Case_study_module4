package com.codegym.fashionshop.service;

import com.codegym.fashionshop.dto.CartDto;
import com.codegym.fashionshop.dto.CheckoutDto;
import com.codegym.fashionshop.model.Order;
import com.codegym.fashionshop.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IOrderService {

    List<Order> findAll(Specification<Order> spec);

    Order findById(Long id);

    Order findByIdWithDetails(Long id);

    void updateStatus(Long orderId, String status);

    // Các phương thức đếm cho dashboard
    long countTotalOrders();
    long countPendingOrders();

    void placeOrder(CartDto cart, CheckoutDto checkoutInfo, User currentUser);

    List<Order> findByUser(User user);
}