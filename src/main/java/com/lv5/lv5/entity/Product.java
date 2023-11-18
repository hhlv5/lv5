package com.lv5.lv5.entity;

import com.lv5.lv5.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price; // String 이였어요

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "info")
    private String info;

    @Column(name = "category")
    private String category;

    public Product(ProductRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.quantity = requestDto.getQuantity();
        this.info = requestDto.getInfo();
        this.category = requestDto.getCategory();
    }

}
