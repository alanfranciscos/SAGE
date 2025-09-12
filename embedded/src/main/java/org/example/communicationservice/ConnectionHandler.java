package org.example.communicationservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionHandler implements Runnable {

    private Socket socket;

    private int i = 0;
    private volatile long lastKeepAliveTime = System.currentTimeMillis();
    private volatile boolean online = true;

    private byte seq;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
        this.seq = 0x00;

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            long diff = System.currentTimeMillis() - lastKeepAliveTime;
            if (diff > 5 * 60 * 1000) {
                if (online) {
                    online = false;
                    System.out.println("⚠Central OFFLINE (sem keep alive por 5 minutos)");
                }
            } else {
                if (!online) {
                    online = true;
                    System.out.println("Central voltou ONLINE");
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
                System.out.print("Recebido: ");
                printHex(buffer, bytesRead, "Recebido: ");

                if (!validateChecksum(buffer, bytesRead)) {
                    System.out.println("Checksum inválido. Pacote ignorado.");
                    continue;
                }

                byte cmd = buffer[3];
                this.seq = buffer[2];

                switch (cmd) {
                    case 0x21:
                        System.out.println("CONVERTENDO HEX PARA ASCII (0x21): " + bytesToAscii(buffer, bytesRead));
                        verifyModel(buffer);
                        respondConnection(outputStream, seq, cmd);
                        break;

                    case 0x40:
                        lastKeepAliveTime = System.currentTimeMillis();
                        respondKeepAlive(outputStream, seq, cmd);
                        break;

                    case 0x24:
                        System.out.println("CONVERTENDO HEX PARA ASCII (0x24): " + bytesToAscii(buffer, bytesRead));
                        verifyEvent(buffer);
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
        response[6] = calculateChecksum(response);
        sendResponse(out, response, "Resposta CONEXÃO enviada");
    }

    private void respondKeepAlive(OutputStream out, byte seq, byte cmd) throws IOException {
        byte[] response = new byte[]{
                0x7B, 0x06, seq, cmd, 0x05, 0x00
        };
        response[5] = calculateChecksum(response);
        sendResponse(out, response, "Resposta KEEP ALIVE enviada");

    }

    private void makePostRequest(int usuario) {
        // https://www.twilio.com/pt-br/blog/5-maneiras-de-fazer-uma-chamada-http-em-java
        // https://stackoverflow.com/questions/75966165/how-to-replace-the-deprecated-url-constructors-in-java-20
        try {
            HttpClient client = HttpClient.newHttpClient();

            String jsonBody = "{"
                    + "\"controlId\": \"" + 1 + "\","
                    + "\"calledAt\": \"" + System.currentTimeMillis() + "\""
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/assist"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: " + response.statusCode());
            System.out.println("Resposta: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void respondEvent(OutputStream out, byte[] buffer, byte seq, byte cmd) throws IOException {
        byte[] contador = new byte[]{buffer[17], buffer[18], buffer[19], buffer[20]};
        byte[] response = new byte[]{
                0x7B, 0x0A, seq, cmd, 0x01,
                contador[0], contador[1], contador[2], contador[3], 0x00
        };
        response[9] = calculateChecksum(response);
        if (buffer[8] == 0x31 && buffer[9] == 0x31 && buffer[10] == 0x30 && buffer[11] == 0x30) {
            System.out.println("EMERGÊNCIA MÉDICA ACIONADA");
            System.out.println(i++);
            StringBuilder usuario = new StringBuilder();
            usuario.append((char) buffer[14]);
            usuario.append((char) buffer[15]);
            usuario.append((char) buffer[16]);
            System.out.println("Usuário: " + usuario.toString());
            makePostRequest(Integer.parseInt(usuario.toString()));
        }

        sendResponse(out, response, "Resposta EVENTO enviada");
    }

    private void sendResponse(OutputStream out, byte[] response, String message) throws IOException {
        out.write(response);
        out.flush();
        printHex(response, response.length, "Enviado: ");
        System.out.println(message);
    }

    private void printHex(byte[] data, int length, String prefix) {
        String timestamp = java.time.LocalTime.now().withNano(0).toString();
        System.out.print("[" + timestamp + "] " + prefix);
        for (int i = 0; i < length; i++) {
            System.out.printf("0x%02X ", data[i]);
        }
        System.out.println();
    }

    private byte calculateChecksum(byte[] data) {
        byte checksum = 0;
        for (byte b : data) {
            checksum ^= b;
        }
        return checksum;
    }

    private boolean validateChecksum(byte[] data, int length) {
        byte checksum = 0;
        for (int i = 0; i < length; i++) {
            checksum ^= data[i];
        }
        return checksum == 0;
    }

    private void verifyModel(byte[] buffer) {
        System.out.println("=================x=================");
        byte modelo = (byte) buffer[41];
        switch (modelo) {
            case (byte) 0xA0:
                System.out.println("Active 32 Duo");
                break;
            case (byte) 0xA1:
                System.out.println("Active 20 Ultra");
                break;
            case (byte) 0xA2:
                System.out.println("Active 8 Ultra");
                break;
            case (byte) 0xA3:
                System.out.println("Active 20 Ethernet");
                break;
            case (byte) 0xA4:
                System.out.println("Active 100 Bus");
                break;
            case (byte) 0xA5:
                System.out.println("Active 20 Bus");
                break;
            case (byte) 0xA6:
                System.out.println("Active Full 32");
                break;
            case (byte) 0xA7:
                System.out.println("Active 20");
                break;
            case (byte) 0xA9:
                System.out.println("Active 8W");
                break;
            default:
                System.out.println("Modelo desconhecido");
                break;
        }
        System.out.println("=================x=================");
    }

    private void verifyEvent(byte[] buffer) {
        StringBuilder evento = new StringBuilder();
        evento.append((char) buffer[8]);
        evento.append((char) buffer[9]);
        evento.append((char) buffer[10]);
        evento.append((char) buffer[11]);
        System.out.println("EVENTO: " + evento.toString());
    }

    private String bytesToAscii(byte[] data, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // Mostra apenas caracteres "printáveis" (32 a 126)
            if (data[i] >= 32 && data[i] <= 126) {
                sb.append((char) data[i]);
            } else {
                sb.append("."); // substitui não-printáveis por ponto
            }
        }
        return sb.toString();
    }
}
