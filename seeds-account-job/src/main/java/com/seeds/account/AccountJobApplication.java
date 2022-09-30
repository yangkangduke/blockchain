package com.seeds.account;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.seeds.account.service.TaskMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Slf4j
@EnableFeignClients(basePackages = "com.seeds")
@SpringBootApplication(scanBasePackages = "com.seeds")
@EnableMethodCache(basePackages = "com.seeds")
@EnableCreateCacheAnnotation
public class AccountJobApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AccountJobApplication.class, args);
    }

    @Autowired
    TaskMonitorService taskMonitorService;

    @Override
    public void run(String... args) throws Exception {
    }
}
