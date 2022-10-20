package com.seeds.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author yk
 */
@MapperScan("com.seeds.account.mapper")
@EnableFeignClients(basePackages = "com.seeds")
@SpringBootApplication(scanBasePackages = "com.seeds")
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

}
