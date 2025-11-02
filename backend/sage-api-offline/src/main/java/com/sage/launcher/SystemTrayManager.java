package com.sage.launcher;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URI;

@Component
public class SystemTrayManager implements CommandLineRunner {

    @Override
    public void run(String... args) {
        new Thread(() -> {
            try {
                Thread.sleep(3000);

                if (!SystemTray.isSupported()) {
                    System.out.println("Bandeja do sistema não suportada neste ambiente.");
                    return;
                }

                EventQueue.invokeLater(() -> {
                    try {
                        SystemTray tray = SystemTray.getSystemTray();
                        Image image;

                        try (InputStream iconStream = getClass().getResourceAsStream("/icon.png")) {
                            if (iconStream == null) {
                                System.err.println("Ícone não encontrado em /icon.png");
                                return;
                            }
                            image = ImageIO.read(iconStream);
                        }

                        PopupMenu popup = new PopupMenu();
                        MenuItem openItem = new MenuItem("Abrir SAGE");
                        MenuItem exitItem = new MenuItem("Encerrar");

                        openItem.addActionListener((ActionEvent e) -> {
                            try {
                                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });

                        exitItem.addActionListener((ActionEvent e) -> {
                            tray.remove(tray.getTrayIcons()[0]);
                            System.exit(0);
                        });

                        popup.add(openItem);
                        popup.addSeparator();
                        popup.add(exitItem);

                        TrayIcon trayIcon = new TrayIcon(image, "SAGE", popup);
                        trayIcon.setImageAutoSize(true);
                        tray.add(trayIcon);

                        trayIcon.displayMessage(
                                "SAGE iniciado",
                                "O sistema está rodando. Clique aqui para abrir.",
                                TrayIcon.MessageType.INFO
                        );

                        trayIcon.addActionListener((ActionEvent e) -> {
                            try {
                                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });

                        System.out.println("SAGE rodando na bandeja do sistema.");

                        // Abre o navegador automaticamente após iniciar
                        new Thread(() -> {
                            try {
                                Thread.sleep(2000);
                                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
                            } catch (Exception ignored) {}
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}