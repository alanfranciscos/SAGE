package org.example.api;

import org.example.model.AlarmPanel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {

    // https://www.twilio.com/pt-br/blog/5-maneiras-de-fazer-uma-chamada-http-em-java
    // https://stackoverflow.com/questions/75966165/how-to-replace-the-deprecated-url-constructors-in-java-20

    public void makeUserPostRequest(int usuario) {

        try {
            HttpClient client = HttpClient.newHttpClient();

            String jsonBody = "{"
                    + "\"controlId\": \"" + usuario + "\","
                    + "\"calledAt\": \"" + System.currentTimeMillis() + "\""
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/assist"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status do envio de emergencia medica: " + response.statusCode());
            System.out.println("Resposta do envio de emergencia medica: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makePanelInfoPostRequest(AlarmPanel alarmPanel) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String jsonBody = "{"
                    + "\"serial_number\": \"" + alarmPanel.getSerialNumber() + "\","
                    + "\"count_number\": \"" + alarmPanel.getCount() + "\""
                    + "\"ip_address\": \"" + alarmPanel.getIpAdress() + "\""
                    + "\"mac_address\": \"" + alarmPanel.getMacAdress() + "\""
                    + "\"model\": \"" + alarmPanel.getModel() + "\""
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/alarms/serial/" + alarmPanel.getSerialNumber()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status do envio de dados da central: " + response.statusCode());
            System.out.println("Resposta do envio de dados da central: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makePanelInfoGetRequest(String panelSerialNumber) {
        try {
            HttpClient client = HttpClient.newHttpClient();

//            String jsonBody = "{"
//                    + "\"serial_number\": \"" + alarmPanel.getSerialNumber() + "\","
//                    + "\"count_number\": \"" + alarmPanel.getCount() + "\""
//                    + "\"ip_address\": \"" + alarmPanel.getIpAdress() + "\""
//                    + "\"mac_address\": \"" + alarmPanel.getMacAdress() + "\""
//                    + "\"model\": \"" + alarmPanel.getModel() + "\""
//                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/alarms/serial/" + panelSerialNumber))
                    .version(HttpClient.Version.HTTP_2)
                    .GET()
                    .build();

            HttpResponse<String> response = client
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status do envio de dados da central: " + response.statusCode());
            System.out.println("Resposta do envio de dados da central: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makePanelConnectionStatusPostRequest(boolean online) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String jsonBody = "{"
                    + "\"connection_status\": \"" + online + "\","
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/assist"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status do envio de status de conexão da central: " + response.statusCode());
            System.out.println("Resposta do envio de status de conexão da central: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
