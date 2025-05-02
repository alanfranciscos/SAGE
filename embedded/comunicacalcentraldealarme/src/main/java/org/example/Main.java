package org.example;

public class Main {
    public static void main(String[] args) {
        int porta = 6045;
        try {
            JFLCentralListener listener = new JFLCentralListener(porta);
            listener.listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
