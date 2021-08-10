package com.github.wuchao.microservicedemo.controller;

import com.github.wuchao.microservicedemo.dto.Product;
import com.github.wuchao.microservicedemo.feign.ProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private ProductClient productClient;


    @GetMapping("/orders/{orderId}/product")
    public ResponseEntity orderProduct(@PathVariable("orderId") Long orderId) {

        Long productId = 1L;
        Product product = productClient.getProduct(productId).getBody();
        return ResponseEntity.ok(product);
    }

}
