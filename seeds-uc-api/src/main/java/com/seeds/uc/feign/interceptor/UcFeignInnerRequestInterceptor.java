package com.seeds.uc.feign.interceptor;

import com.seeds.common.web.HttpHeaders;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * feign配置拦截
 *
 * @author hang.yu
 * @date 2022/08/09
 **/
@Component
public class UcFeignInnerRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(HttpHeaders.INNER_REQUEST, Boolean.TRUE.toString());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        // 国际化
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            // 传递header信息
            template.header("accept-language", request.getHeader("accept-language"));
            template.header("cookie", request.getHeader("cookie"));

            // 传递lang
            String lang = request.getParameter("lang");
            if (StringUtils.isNotBlank(lang)) {
                template.query("lang", lang);
            }
        }

    }
}