package com.lv5.lv5.controller;

import com.lv5.lv5.dto.ProductRequestDto;
import com.lv5.lv5.dto.ProductResponseDto;
import com.lv5.lv5.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/register")
    public ResponseEntity registerProduct(@RequestBody ProductRequestDto requestDto){
        return new ResponseEntity(productService.registerProduct(requestDto), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity findProduct(@PathVariable Long productId){
        return new ResponseEntity(productService.findProduct(productId), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<ProductResponseDto>> findProducts(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false, defaultValue = "name", value = "orderby") String criteria,
            @RequestParam(required = false, defaultValue = "DESC", value = "sort") String sort)
    {
        return new ResponseEntity<>(productService.getProducts(pageNo, criteria, sort),HttpStatus.OK);
    }
}
