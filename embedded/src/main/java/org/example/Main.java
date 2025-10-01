package org.example;

import org.example.api.HttpService;
import org.example.communicationservice.TCPServerSocket;

public class Main {
    public static void main(String[] args) {
        HttpService httpService = new HttpService();
        int port = httpService.getPortBySerialNumber("2785040674");
        System.out.println("PORTA: " + port);
        try {
            TCPServerSocket listener = new TCPServerSocket(port);
            listener.listen();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}