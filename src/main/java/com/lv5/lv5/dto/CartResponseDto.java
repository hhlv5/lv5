package com.lv5.lv5.dto;

import java.util.HashMap;
import java.util.Map;

import com.lv5.lv5.entity.Cart;
import com.lv5.lv5.entity.CartProduct;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartResponseDto {
	@Positive
	private Long cartId;
	private Integer productsCount;
	private Integer totalPrice;
	private Map<ProductResponseDto, Integer> products = new HashMap<>();

	public CartResponseDto(Cart cart) {
		this.cartId = cart.getId();
		this.productsCount = cart.getCartProducts().stream().mapToInt(CartProduct::getQuantity).sum();
		this.totalPrice = cart.getCartProducts().stream().mapToInt(CartResponseDto::calculateTotalPrice).sum();
		cart.getCartProducts()
			.forEach((cartProducts) ->
				products.put(new ProductResponseDto(cartProducts.getProduct()), cartProducts.getQuantity())
			);
	}

	private static int calculateTotalPrice(CartProduct cartProduct) {
		return cartProduct.getProduct().getPrice() * cartProduct.getQuantity();
	}
}
