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

    // TO DO. 수정 필
    public ProductResponseDto findProduct(Long productId) {
        return new ProductResponseDto(productRepository.findProduct(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다.")));
    }

    public List<ProductResponseDto> getProducts() {
        return productRepository.findAll().stream().map(ProductResponseDto::new).toList();
    }
}
