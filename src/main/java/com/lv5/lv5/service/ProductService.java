package com.lv5.lv5.service;

import com.lv5.lv5.dto.ProductRequestDto;
import com.lv5.lv5.dto.ProductResponseDto;
import com.lv5.lv5.entity.Product;
import com.lv5.lv5.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponseDto registerProduct(ProductRequestDto requestDto) {
        return new ProductResponseDto(productRepository.save(new Product(requestDto)));
    }

    public List<ProductResponseDto> getProducts() {
        return productRepository.findAll().stream().map(ProductResponseDto::new).toList();
    }

    public ProductResponseDto getProduct(Long productId) {
        return new ProductResponseDto(productRepository.findProductByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다.")));
    }
}
