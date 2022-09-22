package com.seeds.notification.config;

import com.seeds.notification.server.Bootstrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author: hewei
 * @date 2022/9/19
 */
@Configuration
public class WebSocketServerConfig {
    @Bean
    public Bootstrap bootstrap() {
        return new Bootstrap();
    }
}
