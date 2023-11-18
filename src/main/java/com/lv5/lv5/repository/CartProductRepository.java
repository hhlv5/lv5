package com.lv5.lv5.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lv5.lv5.entity.Cart;
import com.lv5.lv5.entity.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

	Optional<CartProduct> findByCartAndProductProductId(Cart cart, Long productId);
}
