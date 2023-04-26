package com.seeds.game.feign.interceptor;

import com.seeds.common.web.HttpHeaders;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * feign配置拦截
 *
 * @author hang.yu
 * @date 2022/08/09
 **/
@Component
public class GameFeignInnerRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(HttpHeaders.INNER_REQUEST, Boolean.TRUE.toString());
    }
}