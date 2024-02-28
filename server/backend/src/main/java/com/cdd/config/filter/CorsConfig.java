package com.cdd.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

// 使浏览器允许跨域访问
@Configuration
public class CorsConfig implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // 将ServletRequest强制转换为HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 将ServletResponse强制转换为HttpServletResponse
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 执行CORS相关的设置
        String origin = request.getHeader("Origin");
        if(origin != null){
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        String headers = request.getHeader("Access-Control-Request-Headers");
        if (headers != null) {
            response.setHeader("Access-Control-Allow-Headers", headers);
        }

        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");


        filterChain.doFilter(request, response);
    }

}