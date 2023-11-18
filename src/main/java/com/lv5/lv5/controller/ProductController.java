package com.lv5.lv5.controller;

import com.lv5.lv5.dto.ProductRequestDto;
import com.lv5.lv5.dto.ProductResponseDto;
import com.lv5.lv5.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/register")
    public ResponseEntity registerProduct(@RequestBody ProductRequestDto requestDto){
        return new ResponseEntity(productService.registerProduct(requestDto), HttpStatus.OK);
    }

    @GetMapping("/{Id})")
    public ProductResponseDto findProduct(@PathVariable Long Id){
        return productService.findProduct(Id);
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponseDto>> findProducts(){
        return new ResponseEntity<>(productService.getProducts(),HttpStatus.OK);
    }
}
