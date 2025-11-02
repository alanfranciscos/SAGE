package com.sage.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{path:^(?!api|error|assets|media)[^\\.]*}")
                .setViewName("forward:/index.html");

        registry.addViewController("/{path:^(?!api|error|assets|media)[^\\.]*}/**")
                .setViewName("forward:/index.html");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .mediaType("woff", MediaType.valueOf("font/woff"))
                .mediaType("woff2", MediaType.valueOf("font/woff2"))
                .mediaType("ttf", MediaType.valueOf("font/ttf"))
                .mediaType("otf", MediaType.valueOf("font/otf"))
                .mediaType("eot", MediaType.valueOf("application/vnd.ms-fontobject"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");

        registry.addResourceHandler("/media/**")
                .addResourceLocations("classpath:/static/media/");
    }
}


