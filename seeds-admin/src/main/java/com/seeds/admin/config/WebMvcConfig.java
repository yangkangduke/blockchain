package com.seeds.admin.config;

import com.seeds.admin.mapping.RequestMappingWrapping;
import com.seeds.common.web.interceptor.AdminUserContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer, WebMvcRegistrations {

    @Autowired
    private AdminUserContextInterceptor adminUserContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> patterns = new ArrayList<>();
        patterns.add("/admin/**");
        registry.addInterceptor(adminUserContextInterceptor).addPathPatterns(patterns);
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new RequestMappingWrapping();
    }

}
