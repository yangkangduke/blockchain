package com.seeds.game.config;

import com.seeds.game.interceptor.OpenContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer, WebMvcRegistrations {

    @Autowired
    private OpenContextInterceptor openContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(openContextInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "/v2/api-docs", "/v3/api-docs");
    }

}
