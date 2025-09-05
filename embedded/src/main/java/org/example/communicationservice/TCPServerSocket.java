package org.example.communicationservice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerSocket {

    private ServerSocket serverSocket;
    private int port;

    public TCPServerSocket(int port) throws IOException{
        this.serverSocket = new ServerSocket(port);
        this.port = port;
        System.out.println("Aguardando conexão na porta " + port + "...");
    }

    public void listen(){
        while (true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("Conexão recebida da central: " + socket.getInetAddress().getHostAddress());
                new Thread(new ConnectionHandler(socket)).start();
            } catch(Exception e){
                System.out.println("Erro na conexão: " + e.getMessage());
            }
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
