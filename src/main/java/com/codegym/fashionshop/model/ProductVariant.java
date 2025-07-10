package com.codegym.fashionshop.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String size;

    @Column(nullable = false)
    private int quantity = 0; // Số lượng tồn kho

    @Column(nullable = false)
    private int soldQuantity = 0; // Số lượng đã bán

    // Quan hệ Nhiều-Một: Nhiều biến thể thuộc về một sản phẩm
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}