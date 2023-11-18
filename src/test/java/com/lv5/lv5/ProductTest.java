package com.lv5.lv5;

import com.lv5.lv5.dto.ProductResponseDto;
import com.lv5.lv5.entity.Product;
import com.lv5.lv5.repository.ProductRepository;
import com.lv5.lv5.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
