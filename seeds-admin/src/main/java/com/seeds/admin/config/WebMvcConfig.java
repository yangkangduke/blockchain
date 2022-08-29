package com.seeds.admin.config;

import com.seeds.common.web.interceptor.AdminUserContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer, WebMvcRegistrations {

    @Autowired
    private AdminUserContextInterceptor adminUserContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminUserContextInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/**", "/public/**", "/swagger-resources/**", "/v2/api-docs", "/v3/api-docs");
    }

}
