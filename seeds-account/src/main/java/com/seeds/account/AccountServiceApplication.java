package com.seeds.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author yk
 */
@MapperScan("com.seeds.account.mapper")
@EnableFeignClients(basePackages = "com.seeds")
@SpringBootApplication(scanBasePackages = "com.seeds")
@EnableScheduling
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

}
