package com.codegym.fashionshop.service.impl;

import com.codegym.fashionshop.dto.CartDto;
import com.codegym.fashionshop.dto.CheckoutDto;
import com.codegym.fashionshop.model.Order;
import com.codegym.fashionshop.model.OrderDetail;
import com.codegym.fashionshop.model.ProductVariant;
import com.codegym.fashionshop.model.User;
import com.codegym.fashionshop.repository.OrderRepository;
import com.codegym.fashionshop.repository.ProductVariantRepository;
import com.codegym.fashionshop.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

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
    public Order findByIdWithDetails(Long id) {
        return orderRepository.findByIdWithDetails(id).orElse(null);
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

    @Override
    @Transactional // Đảm bảo tất cả các thao tác được thực hiện thành công hoặc rollback
    public void placeOrder(CartDto cart, CheckoutDto checkoutInfo, User currentUser) {
        // 1. Tạo đối tượng Order
        Order order = new Order();
        order.setCustomerName(checkoutInfo.getCustomerName());
        order.setCustomerPhone(checkoutInfo.getCustomerPhone());
        order.setShippingAddress(checkoutInfo.getShippingAddress());
        order.setCustomerEmail(checkoutInfo.getCustomerEmail());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING"); // Trạng thái chờ xử lý
        order.setTotalAmount(cart.getTotalPrice());
        order.setUser(currentUser); // Gán người dùng hiện tại (có thể null nếu không đăng nhập)

        // 2. Tạo các OrderDetail từ CartItem và cập nhật tồn kho
        Set<OrderDetail> orderDetails = new HashSet<>();
        cart.getItems().forEach((variantId, item) -> {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());

            // Lấy variant từ DB để đảm bảo dữ liệu mới nhất
            ProductVariant variant = variantRepository.findByIdWithProduct(variantId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            detail.setProductVariant(variant);

            // Cập nhật tồn kho
            int newQuantity = variant.getQuantity() - item.getQuantity();
            if (newQuantity < 0) {
                throw new RuntimeException("Số lượng tồn kho không đủ cho sản phẩm: " + variant.getProduct().getName());
            }
            variant.setQuantity(newQuantity);
            variant.setSoldQuantity(variant.getSoldQuantity() + item.getQuantity());
            variantRepository.save(variant); // Lưu lại variant đã cập nhật

            orderDetails.add(detail);
        });

        order.setOrderDetails(orderDetails);

        // 3. Lưu Order (và OrderDetail nhờ CascadeType.ALL)
        orderRepository.save(order);
    }
}