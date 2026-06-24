package com.supermarket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 超市前台销售系统 — Spring Boot 启动类
 * <p>
 * 技术栈：Spring Boot 3.4.5 + MyBatis 3.0.4 + MySQL 8.0
 * 架构：Controller → Service → ServiceImpl → Mapper/XML → Entity 五层分层
 * 前端：原生 HTML + CSS + JavaScript (Ajax)，无前端框架依赖
 * </p>
 *
 * @author 常建航
 * @version 1.0
 */
@SpringBootApplication
@MapperScan("com.supermarket.mapper")  // 自动扫描 Mapper 接口并注册为 Spring Bean
public class SupermarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupermarketApplication.class, args);
    }
}
