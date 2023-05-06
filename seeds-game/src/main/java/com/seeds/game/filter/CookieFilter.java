package com.seeds.game.filter;

import com.seeds.common.web.wrapper.BodyReaderHttpServletRequestWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(filterName = "cookieFilter",urlPatterns = {"/**"})
public class CookieFilter implements Filter {

    private static final String POST_METHOD = "POST";

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (request != null) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            // 遇到post方法才对request进行包装
            String methodType = httpRequest.getMethod();
            // 上传文件时同样不进行包装
            if (POST_METHOD.equalsIgnoreCase(methodType)) {
                requestWrapper = new BodyReaderHttpServletRequestWrapper(
                        (HttpServletRequest) request);
            }
        }
        if (null == requestWrapper) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void destroy() {

    }
}
