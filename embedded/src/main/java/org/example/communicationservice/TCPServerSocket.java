package org.example.communicationservice;

import org.example.api.HttpService;
import org.example.model.AlarmPanel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPServerSocket {

    private ServerSocket serverSocket;
    private int port;

    public TCPServerSocket(int port) throws IOException{
        this.serverSocket = new ServerSocket(port);
        this.port = port;
        System.out.println("Aguardando conexão na porta " + port + "...");
    }

    public void listen() {
        boolean conexaoRecebida = false;
        try {
//            serverSocket.setSoTimeout(300_000); // 5 minutos
            serverSocket.setSoTimeout(30_000); // 30 segundos

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    String alarmPanelIpAddress = socket.getInetAddress().getHostAddress();
                    ConnectionHandler connectionHandler = new ConnectionHandler(alarmPanelIpAddress, socket);
                    new Thread(connectionHandler).start();
                    conexaoRecebida = true;
                } catch (SocketTimeoutException e) {
                    if (!conexaoRecebida) {
                        System.out.println("FALHA: Nenhuma conexão recebida após 30 segundos.");

                        HttpService httpService = new HttpService();
                        String serialNumber = "2785040674";
                        AlarmPanel alarmPanel = httpService.makePanelInfoGetRequest(serialNumber);

                        alarmPanel.setStatus("Offline");

                        httpService.makePanelConnectionStatusPutRequest(serialNumber, "Offline", alarmPanel);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e.getMessage());
        }
    }


    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
