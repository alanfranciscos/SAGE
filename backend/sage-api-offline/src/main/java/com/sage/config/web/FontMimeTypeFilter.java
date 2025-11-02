package com.sage.config.web;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Order(1)
public class FontMimeTypeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();

        if (uri.endsWith(".woff2")) {
            res.setContentType("font/woff2");
        } else if (uri.endsWith(".woff")) {
            res.setContentType("font/woff");
        } else if (uri.endsWith(".ttf")) {
            res.setContentType("font/ttf");
        } else if (uri.endsWith(".otf")) {
            res.setContentType("font/otf");
        } else if (uri.endsWith(".eot")) {
            res.setContentType("application/vnd.ms-fontobject");
        }

        chain.doFilter(request, response);
    }
}