package com.productpool.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** 产品池后端服务启动类 基于 Spring Boot 3.x 构建，提供产品池管理相关的 REST API */
@SpringBootApplication
public class ProductPoolBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductPoolBackendApplication.class, args);
  }
}
