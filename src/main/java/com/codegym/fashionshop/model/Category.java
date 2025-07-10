package com.codegym.fashionshop.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Set;

@Entity @Getter @Setter
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Quan hệ Nhiều-Một với chính nó (nhiều danh mục con thuộc một danh mục cha)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // Quan hệ Một-Nhiều với chính nó (một danh mục cha có nhiều danh mục con)
    @OneToMany(mappedBy = "parent")
    private Set<Category> children;
}