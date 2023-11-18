package com.lv5.lv5;

import com.lv5.lv5.dto.ProductResponseDto;
import com.lv5.lv5.entity.Product;
import com.lv5.lv5.repository.ProductRepository;
import com.lv5.lv5.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class ProductTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;

    @Test
    @DisplayName("상품조회테스트")
    void test1(){
        ProductResponseDto product = productService.findProduct(1L);

        System.out.println(product.getName());
        System.out.println(product.getCategory());
    }

    @Test
    @Rollback(value = false)
    @DisplayName("페이징 테스트")
    void test(){
        Page<ProductResponseDto> responseDtoList =
                productService.getProducts(0, "price", "ASC");

        System.out.println(responseDtoList.get().toList());
    }

    @Test
    @Rollback(value = false)
    @DisplayName("페이징 테스트 오류")
    void test2(){
        Page<ProductResponseDto> responseDtoList =
                productService.getProducts(0, "category", "ASC");

        System.out.println(responseDtoList.get().toList());
    }
}
