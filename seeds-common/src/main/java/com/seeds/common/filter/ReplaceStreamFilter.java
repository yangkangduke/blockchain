//package com.seeds.common.filter;
//
//import com.seeds.common.io.CachedBodyHttpServletRequest;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
///**
// * @author rickierao
// * @email antilaw@yahoo.com
// * @date 2021/1/25
// */
//public class ReplaceStreamFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest((HttpServletRequest) servletRequest);
//        chain.doFilter(cachedRequest, response);
//    }
//}
