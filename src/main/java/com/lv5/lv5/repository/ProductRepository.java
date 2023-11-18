package com.lv5.lv5.repository;

import com.lv5.lv5.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM products where product_id = ? ", nativeQuery = true)
    Optional<Product> findProduct(Long productId);
}
