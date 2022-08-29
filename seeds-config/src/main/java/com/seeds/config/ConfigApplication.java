package com.seeds.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {
    public static void main(String[] args) {
         SpringApplication.run(ConfigApplication.class, args);
    }
}