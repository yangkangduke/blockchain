package com.seeds.uc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@MapperScan({"com.seeds.uc.mapper"})
@EnableFeignClients(basePackages = "com.seeds")
@SpringBootApplication(scanBasePackages = {"com.seeds"})
@EnableScheduling
public class UserCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }
}