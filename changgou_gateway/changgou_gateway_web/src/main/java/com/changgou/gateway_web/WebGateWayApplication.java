package com.changgou.gateway_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by wq120 on 3/24/2020.
 */
@SpringBootApplication
@EnableEurekaClient
public class WebGateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebGateWayApplication.class, args);
    }
}
