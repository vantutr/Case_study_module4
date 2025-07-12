package com.codegym.fashionshop.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    // Quan hệ Nhiều-Một: Nhiều sản phẩm thuộc về một danh mục
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Quan hệ Một-Nhiều: Một sản phẩm có nhiều biến thể (size, số lượng)
    // CascadeType.ALL: Khi xóa sản phẩm, tất cả các biến thể của nó cũng sẽ bị xóa.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC") // Sắp xếp các biến thể theo ID tăng dần
    private Set<ProductVariant> variants;

    public boolean isOutOfStock() {
        if (this.variants == null || this.variants.isEmpty()) {
            return true;
        }
        return this.variants.stream().allMatch(variant -> variant.getQuantity() <= 0);
    }
}