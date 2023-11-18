package com.lv5.lv5.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lv5.lv5.dto.CartAddRequestDto;
import com.lv5.lv5.dto.CartResponseDto;
import com.lv5.lv5.dto.ChangeQuantityRequstDto;
import com.lv5.lv5.entity.Cart;
import com.lv5.lv5.entity.CartProduct;
import com.lv5.lv5.entity.Product;
import com.lv5.lv5.entity.User;
import com.lv5.lv5.repository.CartProductRepository;
import com.lv5.lv5.repository.CartRepository;
import com.lv5.lv5.repository.ProductRepository;
import com.lv5.lv5.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "CartService")
@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final CartProductRepository cartProductRepository;

	public CartResponseDto getMyCart(User user) {
		Optional<Cart> findCart = cartRepository.findByUserId(user.getId());
		if (!findCart.isPresent()) { // 아직 user에게 카트가 없음
			Cart cart = new Cart(user);
			return new CartResponseDto(cartRepository.save(cart));
		}
		return new CartResponseDto(findCart.get());
	}

	@Transactional
	public void addToCart(Long productId, CartAddRequestDto cartAddRequestDto, User user) {
		Cart cart;
		Optional<Cart> findCart = cartRepository.findByUserId(user.getId());
		cart = findCart.orElseGet(() -> {
			log.info("회원의 장바구니가 없는 버그 발생");
			return cartRepository.save(new Cart(user));
		});

		int quantity = cartAddRequestDto.getQuantity();

		Optional<CartProduct> findCartProduct = findCartProductInCartProduct(productId, cart);

		if (findCartProduct.isPresent()) { // 이미 장바구니에 존재하는 경우 개수 증가
			addQuantity(findCartProduct.get(), quantity);
		} else { // 장바구니에 해당 상품이 담겨있지 않은 경우
			Product product = productRepository.findProduct(productId)
				.orElseThrow(() -> new NullPointerException("존재하지 않는 상품입니다."));

			CartProduct cartProduct = new CartProduct(product);
			cartProduct.addToCart(cart).updateQuantity(quantity);
			cartProductRepository.save(cartProduct);
		}
	}

	private void addQuantity(CartProduct findCartProduct, int quantity) {
		findCartProduct.addQuantity(quantity);
	}

	private Optional<CartProduct> findCartProductInCartProduct(Long productId, Cart cart) {
		return cart.getCartProducts()
			.stream()
			.filter(cartProduct -> Objects.equals(cartProduct.getProduct().getProductId(), productId))
			.findFirst();
	}

	@Transactional
	public void changeQuantity(Long cartProductId, ChangeQuantityRequstDto changeQuantityRequstDto,
		UserDetailsImpl userDetails) {
		CartProduct findCartProduct = cartProductRepository.findById(cartProductId)
			.orElseThrow(() -> new RuntimeException("해당 상품이 장바구니에 존재하지 않습니다."));

		int changeQuantity = changeQuantityRequstDto.getQuantity();
		findCartProduct.updateQuantity(changeQuantity);

		if (findCartProduct.getQuantity() <= 0) { // 개수 변경 후 0 이하이면 삭제
			cartProductRepository.delete(findCartProduct);
		}
	}

	public void deleteCartProduct(Long cartProductId, UserDetailsImpl userDetails) {
		CartProduct findCartProduct = cartProductRepository.findById(cartProductId)
			.orElseThrow(() -> new RuntimeException("해당 상품이 장바구니에 존재하지 않습니다."));
		cartProductRepository.delete(findCartProduct);
	}
}
