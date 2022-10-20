package com.seeds.wallet.config;

import com.seeds.common.web.HttpHeaders;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: hewei
 * @date 2022/10/19
 */

@Component
public class WalletFeignInnerRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(HttpHeaders.INNER_REQUEST, Boolean.TRUE.toString());
    }
}