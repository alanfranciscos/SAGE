package com.sage.hardware;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AlarmCentralRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("🚨 Iniciando Central de Alarmes...");
        new Thread(() -> {
            try {
                org.example.Main.main(new String[]{});
                System.out.println("✅ Central de Alarmes iniciada com sucesso!");
            } catch (Exception e) {
                System.err.println("❌ Falha ao iniciar Central de Alarmes:");
                e.printStackTrace();
            }
        }, "AlarmCentralThread").start();
    }
}
