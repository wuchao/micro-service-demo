package com.github.wuchao.microservicedemo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {


    @GetMapping("/users/{id}")
    public ResponseEntity getUser(@PathVariable("id") Integer id);

}
