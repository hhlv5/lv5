package com.lv5.lv5.service;

import com.lv5.lv5.dto.ProductRequestDto;
import com.lv5.lv5.dto.ProductResponseDto;
import com.lv5.lv5.entity.Product;
import com.lv5.lv5.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final int DEFAULT_PAGE_SIZE = 5;
    private final ProductRepository productRepository;

    public ProductResponseDto registerProduct(ProductRequestDto requestDto) {
        return new ProductResponseDto(productRepository.save(new Product(requestDto)));
    }

    public ProductResponseDto findProduct(Long productId) {
        return new ProductResponseDto(productRepository.findProduct(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다.")));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(int pageNo, String criteria, String sort){
        checkCriteria(criteria);
        Pageable pageable = (sort.equals("ASC")) ?
                PageRequest.of(pageNo, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.ASC, criteria))
                : PageRequest.of(pageNo, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, criteria));

        Page<Product> products = productRepository.findAllProduct(pageable);
        return products.map(ProductResponseDto::new);
    }

    private void checkCriteria(String criteria){
        if (criteria.equals("price") || criteria.equals("name")){
            return;
        }
        throw new IllegalArgumentException("잘못된 request 입니다.");
    }

}
