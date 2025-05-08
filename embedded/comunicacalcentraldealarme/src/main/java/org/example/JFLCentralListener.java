package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class JFLCentralListener {
    private ServerSocket serverSocket;

    public JFLCentralListener(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        System.out.println("Aguardando conexão na porta " + port + "...");
    }

    public void listen() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();

                System.out.println("Conexão recebida da central: " + socket.getInetAddress().getHostAddress());

                new Thread(new ConnectionHandler(socket)).start();

            } catch (IOException e) {
                System.out.println("Erro na conexão: " + e.getMessage());
            }
        }
    }

    private static class ConnectionHandler implements Runnable {
        private Socket socket;
        private boolean conectado = false;

        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    System.out.print("Dados recebidos: ");
                    for (int i = 0; i < bytesRead; i++) {
                        System.out.printf("%02X ", buffer[i]);
                    }
                    System.out.println();

                    if (bytesRead >= 5) {
                        byte cmd = buffer[3];

                        if (cmd == 0x21 || cmd == 0x2A) {
                            // Comando de Conexão (0x21 ou 0x2A) - envia uma única vez
                            if (!conectado) {
                                responderConexao(outputStream, buffer);
                                conectado = true;
                            }
                        } else if (cmd == 0x40) {
                            // Keep Alive (0x40) - responde continuamente
                            responderKeepAlive(outputStream, buffer);
                        } else if (cmd == 0x24) {
                            // Evento (0x24)
                            responderEvento(outputStream, buffer);
                        } else {
                            // Comando desconhecido, resposta padrão
                            responderErro(outputStream);
                        }
                    }
                }

                socket.close();
                System.out.println("Conexão encerrada.");
            } catch (IOException e) {
                System.out.println("Erro ao processar a conexão: " + e.getMessage());
            }
        }
        private void responderConexao(OutputStream outputStream, byte[] buffer) throws IOException {
            byte seq = buffer[2];
            byte cmd = buffer[3];

            byte[] resposta = new byte[8];
            resposta[0] = (byte) 0x7B;
            resposta[1] = 0x07;
            resposta[2] = seq;
            resposta[3] = cmd;
            resposta[4] = 0x01;
            resposta[5] = 0x05;
            resposta[6] = calcularChecksum(resposta);

            outputStream.write(resposta);
            outputStream.flush();

            int bytesRead = 7;
            System.out.println("Resposta 0x21 (Conexão) enviada.");
            for (int i = 0; i < bytesRead; i++) {
                System.out.printf("%02X ", resposta[i]);
            }
            System.out.println();

        }

        private void responderKeepAlive(OutputStream outputStream, byte[] buffer) throws IOException {
            byte seq = buffer[2];
            byte cmd = buffer[3];

            byte[] resposta = new byte[8];
            resposta[0] = (byte) 0x7B;
            resposta[1] = 0x06;
            resposta[2] = seq;
            resposta[3] = cmd;
            resposta[4] = 0x05;
            resposta[5] = calcularChecksum(resposta);

            outputStream.write(resposta);
            outputStream.flush();

            int bytesRead = 6;
            System.out.println("Resposta 0x40 (Keep Alive) enviada.");
            for (int i = 0; i < bytesRead; i++) {
                System.out.printf("%02X ", resposta[i]);
            }
            System.out.println();
        }

        private void responderEvento(OutputStream outputStream, byte[] buffer) throws IOException {
            byte seq = buffer[2];
            byte cmd = buffer[3];

            byte[] resposta = new byte[10];
            resposta[0] = (byte) 0x7B;
            resposta[1] = 0x0A;
            resposta[2] = seq;
            resposta[3] = cmd;
            resposta[4] = 0x01;
            resposta[5] = buffer[17];
            resposta[6] = buffer[18];
            resposta[7] = buffer[19];
            resposta[8] = buffer[20];
            resposta[9] = calcularChecksum(resposta);

            outputStream.write(resposta);
            outputStream.flush();

            int bytesRead = 10;
            System.out.println("Resposta 0x24 (Evento) enviada.");
            for (int i = 0; i < bytesRead; i++) {
                System.out.printf("%02X ", resposta[i]);
            }
            System.out.println();

        }

        private void responderErro(OutputStream outputStream) throws IOException {
            byte[] respostaErro = new byte[]{(byte) 0x7B, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            outputStream.write(respostaErro);
            outputStream.flush();
            System.out.println("Resposta de erro enviada.");
        }

        private byte calcularChecksum(byte[] dados) {
            byte checksum = 0;
            for (byte b : dados) {
                checksum ^= b;
            }
            return checksum;
        }
    }
}
