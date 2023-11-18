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

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final CartProductRepository cartProductRepository;

	public CartResponseDto getMyCart(User user) {
		Optional<Cart> findCart = cartRepository.findByUserId(user.getId());
		if(!findCart.isPresent()){ // 아직 user에게 카트가 없음
			Cart cart = new Cart(user);
			return new CartResponseDto(cartRepository.save(cart));
		}
		return new CartResponseDto(findCart.get());
	}

	@Transactional
	public void addToCart(CartAddRequestDto cartAddRequestDto, User user) {
		Cart cart = cartRepository.findByUserId(user.getId())
			.orElseThrow(() -> new NullPointerException("장바구니를 찾을 수 없습니다."));

		Long productId = cartAddRequestDto.getProductId();
		int quantity = cartAddRequestDto.getQuantity();

		Optional<CartProduct> findCartProduct = cart.getCartProducts().stream()
			.filter(cartProduct -> Objects.equals(cartProduct.getProduct().getProductId(), productId))
			.findFirst();

		if (findCartProduct.isPresent()) { // 존재하는 경우 개수만 증가
			findCartProduct.get().addQuantity(quantity);
		} else { // 장바구니에 해당 상품이 담겨있지 않은 경우
			Product product = productRepository.findProductByProductId(productId)
				.orElseThrow(() -> new NullPointerException("존재하지 않는 상품입니다."));

			CartProduct cartProduct = new CartProduct(product);
			cartProduct.addToCart(cart).addQuantity(quantity);
			cartProductRepository.save(cartProduct);
		}
	}

	@Transactional
	public void changeQuantity(Long cartProductId, ChangeQuantityRequstDto changeQuantityRequstDto,
		UserDetailsImpl userDetails) {
		CartProduct findCartProduct = cartProductRepository.findById(cartProductId)
			.orElseThrow(() -> new RuntimeException("해당 상품이 장바구니에 존재하지 않습니다."));

		int changeQuantity = changeQuantityRequstDto.getQuantity();
		findCartProduct.updateQuantity(changeQuantity);
	}

	public void deleteCartProduct(Long cartProductId, UserDetailsImpl userDetails) {
		CartProduct findCartProduct = cartProductRepository.findById(cartProductId)
			.orElseThrow(() -> new RuntimeException("해당 상품이 장바구니에 존재하지 않습니다."));
		cartProductRepository.delete(findCartProduct);
	}
}
