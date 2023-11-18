package com.lv5.lv5.dto;

import com.lv5.lv5.entity.Product;

import lombok.Getter;

@Getter
public class ProductResponseDto {
    private String name;
    private int price;
    private int quantity;
    private String info;
    private String category;

    public ProductResponseDto(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.info = product.getInfo();
        this.category = product.getCategory();
    }
}
