package com.github.wuchao.microservicedemo.feign;

import com.github.wuchao.microservicedemo.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE", url = "${spring-cloud.product-service-url}")
public interface ProductClient {

    @GetMapping("/api/products/{productId}")
    ResponseEntity<Product> getProduct(@PathVariable("productId") Long productId);

}
