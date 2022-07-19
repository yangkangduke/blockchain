package com.seeds.common.web.xss;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS过滤
 * @author hang.yu
 * @date 2022/7/19
 */
public class XssFilter implements Filter {

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
				(HttpServletRequest) request);
		chain.doFilter(xssRequest, response);
	}

	@Override
	public void destroy() {
	}

}