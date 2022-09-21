package com.seeds.wallet.config;

import com.seeds.common.interceptor.ServerApiAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.LinkedList;
import java.util.List;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/25
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private ServerApiAuthInterceptor apiAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePatterns = new LinkedList<>();
        excludePatterns.add("/info");
        excludePatterns.add("/health");
        excludePatterns.add("/error");
        registry.addInterceptor(apiAuthInterceptor).excludePathPatterns(excludePatterns);
    }
}
