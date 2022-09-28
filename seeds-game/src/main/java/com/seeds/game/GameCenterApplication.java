package com.seeds.game;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hang.yu
 * @date 2022/9/27
 */
@MapperScan({"com.seeds.game.mapper"})
@SpringBootApplication(scanBasePackages = {"com.seeds"})
public class GameCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameCenterApplication.class, args);
    }
}