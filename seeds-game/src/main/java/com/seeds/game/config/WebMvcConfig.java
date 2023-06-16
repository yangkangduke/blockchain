package com.seeds.game.config;

import com.seeds.game.interceptor.OpenContextInterceptor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer, WebMvcRegistrations {

    @Bean
    public LocaleResolver localeResolver()
    {
        //(1)Cookie方式
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setCookieName("localeCookie");
        //设置默认区域
        localeResolver.setDefaultLocale(Locale.US);
        localeResolver.setCookieMaxAge(3600);//设置cookie有效期.
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor()
    {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名实现国际化效果
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(getOpenContextInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**", "/v2/api-docs", "/v3/api-docs");
    }

    @Bean
    public OpenContextInterceptor getOpenContextInterceptor() {
        return new OpenContextInterceptor();
    }

}
