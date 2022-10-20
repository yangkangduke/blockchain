package com.seeds.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@EnableFeignClients(basePackages = "com.seeds")
@SpringBootApplication(scanBasePackages = "com.seeds")
//@EnableMethodCache(basePackages = "com.seeds")
//@EnableCreateCacheAnnotation
public class AccountJobApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AccountJobApplication.class, args);
    }

//    @Autowired
//    TaskMonitorService taskMonitorService;

    @Override
    public void run(String... args) throws Exception {
    }
}
