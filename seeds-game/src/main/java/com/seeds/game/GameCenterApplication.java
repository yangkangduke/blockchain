package com.seeds.game;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hang.yu
 * @date 2022/9/27
 */
@MapperScan({"com.seeds.game.mapper"})
@EnableHystrix
@EnableAsync(proxyTargetClass = true)
@EnableFeignClients(basePackages = "com.seeds")
@SpringBootApplication(scanBasePackages = {"com.seeds"})
@EnableScheduling
public class GameCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameCenterApplication.class, args);
    }
}
