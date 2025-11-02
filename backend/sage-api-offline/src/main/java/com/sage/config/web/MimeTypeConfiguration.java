package com.sage.config.web;

import org.springframework.boot.web.server.MimeMappings; // Esta classe resolve a ambiguidade
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory; // Interface genérica
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * Corrige o tipo MIME de fontes locais (.woff, .woff2, .ttf, .eot)
 * para evitar "Failed to decode downloaded font" no navegador.
 */
@Configuration
public class MimeTypeConfiguration implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        // Copia todos os mapeamentos padrão do Tomcat
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);

        // Adiciona/ajusta somente fontes
        mappings.add("woff2", "font/woff2");
        mappings.add("woff", "font/woff");
        mappings.add("ttf", "font/ttf");
        mappings.add("eot", "application/vnd.ms-fontobject");
        mappings.add("otf", "font/otf");

        // Reaplica mapeamentos
        factory.setMimeMappings(mappings);

        System.out.println("✅ MIME types para fontes aplicados (mantendo imagens padrão).");
    }
}