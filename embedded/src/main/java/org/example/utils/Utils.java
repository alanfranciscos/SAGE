package org.example.utils;

public class Utils {

    public static void printHex(byte[] data, int length, String prefix) {
        String timestamp = java.time.LocalTime.now().withNano(0).toString();
        System.out.print("[" + timestamp + "] " + prefix);
        for (int i = 0; i < length; i++) {
            System.out.printf("0x%02X ", data[i]);
        }
        System.out.println();
    }

    public static byte calculateChecksum(byte[] data) {
        byte checksum = 0;
        for (byte b : data) {
            checksum ^= b;
        }
        return checksum;
    }

    public static boolean validateChecksum(byte[] data, int length) {
        byte checksum = 0;
        for (int i = 0; i < length; i++) {
            checksum ^= data[i];
        }
        return checksum == 0;
    }

    public static void verifyModel(byte[] buffer) {
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

    public static void verifyEvent(byte[] buffer) {
        StringBuilder evento = new StringBuilder();
        evento.append((char) buffer[8]);
        evento.append((char) buffer[9]);
        evento.append((char) buffer[10]);
        evento.append((char) buffer[11]);
        System.out.println("EVENTO: " + evento.toString());
    }

    public static String bytesToAscii(byte[] data, int length) {
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
