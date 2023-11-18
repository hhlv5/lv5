package com.lv5.lv5.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lv5.lv5.dto.CartAddRequestDto;
import com.lv5.lv5.dto.CartResponseDto;
import com.lv5.lv5.dto.ChangeQuantityRequstDto;
import com.lv5.lv5.entity.Cart;
import com.lv5.lv5.security.UserDetailsImpl;
import com.lv5.lv5.service.CartService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 정민님이만들면 합치기
@RestController
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;
	@GetMapping("/carts")
	public CartResponseDto getMyCart(@AuthenticationPrincipal UserDetailsImpl userDetails){
		return cartService.getMyCart(userDetails.getUser());
	}

	@PostMapping("/products/cart")
	public ResponseEntity<String> addToCart(CartAddRequestDto cartAddRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
		try{
			cartService.addToCart(cartAddRequestDto, userDetails.getUser());
		} catch (Exception e){
			return ResponseEntity.badRequest().body("장바구니에 담는 도중 오류가 발생했습니다.\n" + e.getMessage());
		}
		return ResponseEntity.ok().body("장바구니에 상품을 담았습니다.");
	}

	@PostMapping("/carts/{cartProductId}")
	public ResponseEntity<String> changeQuantity(
		@PathVariable
		Long cartProductId,
		ChangeQuantityRequstDto changeQuantityRequstDto,
		@AuthenticationPrincipal
		UserDetailsImpl userDetails){
		try{
			cartService.changeQuantity(cartProductId, changeQuantityRequstDto, userDetails);
		} catch (Exception e){
			return ResponseEntity.badRequest().body("담은 개수를 변경하는 도중 오류가 발생했습니다.\n" + e.getMessage());
		}
		return ResponseEntity.ok().body("개수 변경 완료");
	}

	@DeleteMapping("/carts/{cartProductId}")
	public ResponseEntity<String> deleteCartProduct(
		@PathVariable
		Long cartProductId,
		@AuthenticationPrincipal
		UserDetailsImpl userDetails){
		try{
			cartService.deleteCartProduct(cartProductId, userDetails);
		} catch (Exception e){
			return ResponseEntity.badRequest().body("상품을 삭제하는 도중 오류가 발생했습니다.\n" + e.getMessage());
		}
		return ResponseEntity.ok().body("장바구니에서 상품 삭제 완료");
	}
}
