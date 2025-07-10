package com.codegym.fashionshop.model;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}