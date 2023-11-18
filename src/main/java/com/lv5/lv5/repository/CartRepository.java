package com.lv5.lv5.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lv5.lv5.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByUserId(Long id);
}
