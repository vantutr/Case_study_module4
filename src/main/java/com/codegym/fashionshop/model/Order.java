package com.codegym.fashionshop.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Đại diện cho một đơn hàng trong hệ thống.
 * Lưu trữ thông tin tổng quan về đơn hàng và thông tin khách hàng tại thời điểm đặt hàng.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String status; // Ví dụ: PENDING, PROCESSING, COMPLETED, CANCELLED

    @Column(precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // Các thông tin này được "đóng băng" tại thời điểm đặt hàng
    // để đảm bảo tính chính xác của dữ liệu lịch sử.
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;

    /**
     * Quan hệ Nhiều-Một: Nhiều đơn hàng có thể thuộc về một người dùng.
     * `user_id` có thể là null để hỗ trợ khách vãng lai (guest checkout).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Quan hệ Một-Nhiều: Một đơn hàng có nhiều chi tiết đơn hàng (các sản phẩm).
     * CascadeType.ALL: Khi lưu/xóa một Order, các OrderDetail liên quan cũng sẽ được xử lý theo.
     * orphanRemoval = true: Khi một OrderDetail bị xóa khỏi danh sách này, nó cũng sẽ bị xóa khỏi DB.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails;
}