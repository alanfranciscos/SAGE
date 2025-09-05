package org.example;

import org.example.communicationservice.TCPServerSocket;

public class Main {
    public static void main(String[] args) {
        int port = 6045;
        try {
            TCPServerSocket listener = new TCPServerSocket(port);
            listener.listen();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}