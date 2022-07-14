package com.seeds.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author hang.yu
 * @date 2022/7/12
 */
@MapperScan({"com.seeds.admin.mapper", "com.seeds.admin.web.*.mapper"})
@EnableFeignClients(basePackages = "com.seeds")
@SpringBootApplication(scanBasePackages = {"com.seeds"})
public class AdminCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminCenterApplication.class, args);
    }
}