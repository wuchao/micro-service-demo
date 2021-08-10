package com.github.wuchao.microservicedemo.controller;

import com.github.wuchao.microservicedemo.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> product(@PathVariable("productId") Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setName("id为【" + productId + "】产品");
        return ResponseEntity.ok(product);
    }
}
