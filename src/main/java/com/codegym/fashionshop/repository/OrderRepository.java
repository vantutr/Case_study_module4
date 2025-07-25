package com.codegym.fashionshop.repository;

import com.codegym.fashionshop.model.Order;
import com.codegym.fashionshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    long countByStatus(String status);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails od LEFT JOIN FETCH od.productVariant pv LEFT JOIN FETCH pv.product WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderDetails od LEFT JOIN FETCH od.productVariant pv LEFT JOIN FETCH pv.product WHERE o.user = :user ORDER BY o.orderDate DESC")
    List<Order> findByUserWithDetails(@Param("user") User user);
}