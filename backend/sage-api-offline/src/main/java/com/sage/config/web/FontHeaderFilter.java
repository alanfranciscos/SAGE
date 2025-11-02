package com.sage.config.web;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class FontHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResp = (HttpServletResponse) response;

        String path = ((jakarta.servlet.http.HttpServletRequest) request).getRequestURI();

        if (path.startsWith("/assets/webfonts/") &&
                (path.endsWith(".woff2") || path.endsWith(".woff") || path.endsWith(".ttf") || path.endsWith(".eot"))) {
            httpResp.setHeader("X-Content-Type-Options", "");
        }

        chain.doFilter(request, response);
    }
}
