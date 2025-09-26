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

    public static String verifyModel(byte[] buffer) {
        byte modelo = (byte) buffer[41];
        switch (modelo) {
            case (byte) 0xA0:
                return "Active 32 Duo";
            case (byte) 0xA1:
                return "Active 20 Ultra";
            case (byte) 0xA2:
                return "Active 8 Ultra";
            case (byte) 0xA3:
                return "Active 20 Ethernet";
            case (byte) 0xA4:
                return "Active 100 Bus";
            case (byte) 0xA5:
                return "Active 20 Bus";
            case (byte) 0xA6:
                return "Active Full 32";
            case (byte) 0xA7:
                return "Active 20";
            case (byte) 0xA9:
                return "Active 8W";
            default:
                return "Modelo desconhecido";
        }
    }

    public static void verifyEvent(byte[] buffer) {
        StringBuilder evento = new StringBuilder();
        evento.append((char) buffer[8]);
        evento.append((char) buffer[9]);
        evento.append((char) buffer[10]);
        evento.append((char) buffer[11]);
//        System.out.println("EVENTO: " + evento.toString());
    }

    public static String bytesToAscii(byte[] data, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (data[i] >= 32 && data[i] <= 126) {
                sb.append((char) data[i]);
            } else {
                sb.append(".");
            }
        }
        return sb.toString();
    }

}
