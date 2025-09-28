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
    private volatile boolean online = false;
    private byte seq;

    private HttpService httpService;
    private String alarmPanelIpAdress = "Indefinido";
    private String status = "Offline";
    private String serialNumber = "Indefinido";
    private String mac = "Indefinido";
    private String model = "Indefinido";
    private String accountNumber = "Indefinido";
    private AlarmPanel alarmPanel;

    public ConnectionHandler(String alarmPanelIpAdress, Socket socket) {
        this.alarmPanelIpAdress = alarmPanelIpAdress;
        this.socket = socket;
        this.seq = 0x00;
        this.status = "Offline";
        this.online = false;
        this.httpService = new HttpService();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            long diff = System.currentTimeMillis() - lastKeepAliveTime;

            if (diff > 5 * 60 * 1000) {
                if (online) {
                    online = false;
                    status = "Offline";
                    if (alarmPanel != null) {
                        alarmPanel.setStatus(status);
                        printStatus("CENTRAL FICOU OFFLINE");
                        httpService.makePanelConnectionStatusPutRequest(alarmPanel.getSerialNumber(), status, alarmPanel);
                    }
                }
            } else {
                if (!online) {
                    online = true;
                    status = "Online";
                    if (alarmPanel != null) {
                        alarmPanel.setStatus(status);
                        printStatus("CENTRAL VOLTOU ONLINE");
                        httpService.makePanelConnectionStatusPutRequest(alarmPanel.getSerialNumber(), status, alarmPanel);
                    }
                }
            }
        }, 30, 30, TimeUnit.SECONDS);

    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {
            byte[] buffer = new byte[256];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                if (!Utils.validateChecksum(buffer, bytesRead)) {
                    continue;
                }

                byte cmd = buffer[3];
                this.seq = buffer[2];

                switch (cmd) {
                    case 0x21: // Pacote de conexão inicial
                        this.serialNumber = Utils.bytesToAscii(buffer, bytesRead).substring(4, 14);
                        String rawMac = Utils.bytesToAscii(buffer, bytesRead).substring(29, 41);
                        this.mac = rawMac.replaceAll("(.{2})", "$1:").replaceAll(":$", "");
                        this.model = Utils.verifyModel(buffer);

                        this.alarmPanel = new AlarmPanel(alarmPanelIpAdress);
                        this.alarmPanel.setSerialNumber(this.serialNumber);
                        this.alarmPanel.setMacAddress(this.mac);
                        this.alarmPanel.setModel(this.model);

                        this.lastKeepAliveTime = System.currentTimeMillis();
                        this.status = "Online";
                        this.online = true;
                        this.alarmPanel.setStatus(this.status);

                        respondConnection(outputStream, seq, cmd);

                        printStatus("CENTRAL CONECTADA IMEDIATAMENTE");

                        saveOrUpdateCentral(this.alarmPanel);

                        break;

                    case 0x40: // Keep-alive
                        lastKeepAliveTime = System.currentTimeMillis();
                        respondKeepAlive(outputStream, seq, cmd);
                        break;

                    case 0x24: // Evento
                        this.accountNumber = Utils.bytesToAscii(buffer, bytesRead).substring(4, 8);
                        if (alarmPanel != null) {
                            alarmPanel.setAccount(this.accountNumber);
                        }
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

    private void printStatus(String header) {
        System.out.println(header);
        if (alarmPanel != null) {
            System.out.println("IP DA CENTRAL: " + alarmPanel.getIpAddress());
            System.out.println("STATUS DA CENTRAL: " + alarmPanel.getStatus());
            System.out.println("SERIAL DA CENTRAL: " + alarmPanel.getSerialNumber());
            System.out.println("MAC DA CENTRAL: " + alarmPanel.getMacAddress());
            System.out.println("MODELO DA CENTRAL: " + alarmPanel.getModel());
            System.out.println("CONTA DA CENTRAL: " + alarmPanel.getAccount());
            System.out.println("#######################");
        }
    }

    private void respondConnection(OutputStream out, byte seq, byte cmd) throws IOException {
        byte[] response = new byte[]{0x7B, 0x07, seq, cmd, 0x01, 0x05, 0x00};
        response[6] = Utils.calculateChecksum(response);
        sendResponse(out, response, "Resposta CONEXÃO enviada");
    }

    private void sendPanelStatusRequest(OutputStream out, byte seq) throws IOException {
        byte[] request = new byte[]{0x7B, 0x06, seq, 0x00, (byte) 0x93, 0x00};
        request[5] = Utils.calculateChecksum(request);
        sendResponse(out, request, "Solicitação do status enviada");
    }

    private void respondKeepAlive(OutputStream out, byte seq, byte cmd) throws IOException {
        byte[] response = new byte[]{0x7B, 0x06, seq, cmd, 0x05, 0x00};
        response[5] = Utils.calculateChecksum(response);
        sendResponse(out, response, "Resposta KEEP ALIVE enviada");
    }

    private void respondEvent(OutputStream out, byte[] buffer, byte seq, byte cmd) throws IOException {
        byte[] contador = new byte[]{buffer[17], buffer[18], buffer[19], buffer[20]};
        byte[] response = new byte[]{0x7B, 0x0A, seq, cmd, 0x01, contador[0], contador[1], contador[2], contador[3], 0x00};
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
    }

    private void saveOrUpdateCentral(AlarmPanel alarmPanel) {
        if (alarmPanel == null || alarmPanel.getSerialNumber() == null || alarmPanel.getSerialNumber().isEmpty()) {
            System.out.println("Central inválida. Não é possível salvar ou atualizar.");
            return;
        }

        AlarmPanel existingPanel = httpService.makePanelInfoGetRequest(alarmPanel.getSerialNumber());

        if (existingPanel != null && existingPanel.getSerialNumber() != null && !existingPanel.getSerialNumber().isEmpty()) {
            httpService.makePanelConnectionStatusPutRequest(alarmPanel.getSerialNumber(), alarmPanel.getStatus(), alarmPanel);
            System.out.println("Central existente atualizada (PUT): " + alarmPanel.getSerialNumber());
        } else {
            httpService.makePanelInfoPostRequest(alarmPanel);
            System.out.println("Central não existente criada (POST): " + alarmPanel.getSerialNumber());
        }
    }

}
