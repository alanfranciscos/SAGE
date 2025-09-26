package org.example.communicationservice;

import org.example.api.HttpService;
import org.example.model.AlarmPanel;
import org.example.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionHandler implements Runnable {
    private Socket socket;
    private volatile long lastKeepAliveTime = System.currentTimeMillis();
    private volatile boolean online = true;
    private byte seq;
    private AlarmPanel alarmPanel;

    HttpService httpService;
    public ConnectionHandler(String alarmPanelIpAdress, Socket socket) {
        this.socket = socket;
        this.seq = 0x00;
        this.alarmPanel = new AlarmPanel(alarmPanelIpAdress);
        this.httpService = new HttpService();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            long diff = System.currentTimeMillis() - lastKeepAliveTime;
            if (diff > 5 * 60 * 1000) {
                if (online) {
                    online = false;
//                    System.out.println("Central OFFLINE (sem keep alive por 5 minutos)");
                }
            } else {
                if (!online) {
                    online = true;
//                    System.out.println("Central voltou ONLINE");
                }
            }
            httpService.makePanelConnectionStatusPostRequest(online);
        }, 30, 30, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {
            byte[] buffer = new byte[256];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                System.out.print("Recebido: ");
//                Utils.printHex(buffer, bytesRead, "Recebido: ");

                if (!Utils.validateChecksum(buffer, bytesRead)) {
//                    System.out.println("Checksum inválido. Pacote ignorado.");
                    continue;
                }

                byte cmd = buffer[3];
                this.seq = buffer[2];

                switch (cmd) {
                    case 0x21:
//                        System.out.println("CONVERTENDO HEX PARA ASCII (0x21): " + Utils.bytesToAscii(buffer, bytesRead));
                        String serialNumber = Utils.bytesToAscii(buffer, bytesRead).substring(4, 14);
//                        System.out.println("Número: " + numero);

                        String rawMac = Utils.bytesToAscii(buffer, bytesRead).substring(29, 41);
                        String mac = rawMac.replaceAll("(.{2})", "$1:").replaceAll(":$", "");
//                        System.out.println("MAC: " + mac);
                        String model = Utils.verifyModel(buffer);

                        alarmPanel.setSerialNumber(serialNumber);
                        alarmPanel.setMacAdress(mac);
                        alarmPanel.setModel(model);

                        respondConnection(outputStream, seq, cmd);
                        break;

                    case 0x40:
                        lastKeepAliveTime = System.currentTimeMillis();
                        respondKeepAlive(outputStream, seq, cmd);
                        break;

                    case 0x24:
//                        System.out.println("CONVERTENDO HEX PARA ASCII (0x24): " + Utils.bytesToAscii(buffer, bytesRead));
                        String countNumber = Utils.bytesToAscii(buffer, bytesRead).substring(4, 7);
                        alarmPanel.setCount(countNumber);
                        Utils.verifyEvent(buffer);
                        respondEvent(outputStream, buffer, seq, cmd);
                        break;

                    default:
                        System.out.printf("Comando não tratado: 0x%02X\n", cmd);
                }
            }

        } catch (IOException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Conexão encerrada.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void respondConnection(OutputStream out, byte seq, byte cmd) throws IOException {
        byte[] response = new byte[]{
                0x7B, 0x07, seq, cmd, 0x01, 0x05, 0x00
        };
        response[6] = Utils.calculateChecksum(response);
        sendResponse(out, response, "Resposta CONEXÃO enviada");
    }

    private void respondKeepAlive(OutputStream out, byte seq, byte cmd) throws IOException {
        byte[] response = new byte[]{
                0x7B, 0x06, seq, cmd, 0x05, 0x00
        };
        response[5] = Utils.calculateChecksum(response);
        sendResponse(out, response, "Resposta KEEP ALIVE enviada");
    }

    private void respondEvent(OutputStream out, byte[] buffer, byte seq, byte cmd) throws IOException {
        byte[] contador = new byte[]{buffer[17], buffer[18], buffer[19], buffer[20]};
        byte[] response = new byte[]{
                0x7B, 0x0A, seq, cmd, 0x01,
                contador[0], contador[1], contador[2], contador[3], 0x00
        };
        response[9] = Utils.calculateChecksum(response);
        if (buffer[8] == 0x31 && buffer[9] == 0x31 && buffer[10] == 0x30 && buffer[11] == 0x30) {
            StringBuilder usuario = new StringBuilder();
            usuario.append((char) buffer[14]);
            usuario.append((char) buffer[15]);
            usuario.append((char) buffer[16]);
            httpService.makeUserPostRequest(Integer.parseInt(usuario.toString()));
        }

        sendResponse(out, response, "Resposta EVENTO enviada");
    }

    private void sendResponse(OutputStream out, byte[] response, String message) throws IOException {
        out.write(response);
        out.flush();
//        Utils.printHex(response, response.length, "Enviado: ");
//        System.out.println(message);
    }


    private boolean MakePanelInfoPostRequest(){
        if(alarmPanel.getCount() != null
                && alarmPanel.getIpAdress() != null
                && alarmPanel.getModel() != null
                && alarmPanel.getMacAdress() != null
                && alarmPanel.getSerialNumber() != null){
            httpService.makePanelInfoPostRequest(alarmPanel);
            return true;
        } else {
            return false;
        }
    }
}
