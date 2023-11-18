package com.lv5.lv5.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cartproduct")
public class CartProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private Integer quantity;

	public CartProduct(Product product) {
		this.product = product;
	}

	public CartProduct addToCart(Cart cart){
		this.cart = cart;
		// 이미 추가된 상품종류가 아니라면
		if(!this.cart.getCartProducts().contains(this)){
			this.cart.getCartProducts().add(this);
		}
		return this;
	}

	public void updateQuantity(int quantity){
		this.quantity = quantity;
	}
	public void addQuantity(int quantity){
		this.quantity += quantity;
	}
}
