package com.lv5.lv5.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartAddRequestDto {
	private Long productId;
	private int quantity;
}
