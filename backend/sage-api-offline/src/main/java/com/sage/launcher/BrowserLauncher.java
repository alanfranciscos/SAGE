package com.sage.launcher;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class BrowserLauncher implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        try {
            String url = "http://localhost:8080";
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
                System.out.println("Navegador aberto em " + url);
            } else {
                System.out.println("Ambiente sem suporte a Desktop — abra manualmente " + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}