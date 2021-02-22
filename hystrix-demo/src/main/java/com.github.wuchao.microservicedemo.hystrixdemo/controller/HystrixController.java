package com.github.wuchao.microservicedemo.hystrixdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HystrixController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private Environment env;

    @GetMapping("/showDiscoverClientDetail")
    public void test() {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(env.getProperty("spring.application.name"));
        ServiceInstance instance = serviceInstances.get(0);
        System.out.println("\ninstance.getScheme():" + instance.getScheme() +
                "instance.getHost():" + instance.getHost() +
                "\ninstance.getPort():" + instance.getPort() +
                "\ninstance.getServiceId():" + instance.getServiceId() +
                "\ninstance.getInstanceId():" + instance.getInstanceId() +
                "\ninstance.getMetadata():" + instance.getMetadata() +
                "\ninstance.getUri():" + instance.getUri());
        /**
         * instance.getScheme():httpinstance.getHost():192.168.199.164
         * instance.getPort():9001
         * instance.getServiceId():HYSTRIX-DEMO
         * instance.getInstanceId():192.168.199.164:9001
         * instance.getMetadata():{management.port=9001}
         * instance.getUri():http://192.168.199.164:9001
         */
    }

}
