package org.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
            String calledAt = java.time.ZonedDateTime.now().toString();
            String jsonBody = "{"
                    + "\"controlId\": \"" + usuario + "\","
                    + "\"calledAt\": \"" + calledAt + "\""
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/assist"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makePanelInfoPostRequest(AlarmPanel alarmPanel) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String jsonBody = "{"
                    + "\"model\": \"" + alarmPanel.getModel() + "\","
                    + "\"status\": \"" + alarmPanel.getStatus() + "\","
                    + "\"ipAddress\": \"" + alarmPanel.getIpAddress() + "\","
                    + "\"macAddress\": \"" + alarmPanel.getMacAddress() + "\","
                    + "\"account\": \"" + alarmPanel.getAccount() + "\","
                    + "\"serialNumber\": \"" + alarmPanel.getSerialNumber() + "\","
                    + "\"port\": \"" + alarmPanel.getPort() + "\""
                    + "}";

            System.out.println("jsonBody: " + jsonBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/alarms"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status da criação de dados da central: " + response.statusCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AlarmPanel makePanelInfoGetRequest(String panelSerialNumber) {
        AlarmPanel alarmPanel = new AlarmPanel();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/alarms/serial/" + panelSerialNumber))
                    .version(HttpClient.Version.HTTP_2)
                    .GET()
                    .build();

            HttpResponse<String> response = client
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();

            ObjectMapper mapper = new ObjectMapper();

            alarmPanel = mapper.readValue(jsonResponse, AlarmPanel.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return alarmPanel;
    }

    public void makePanelConnectionStatusPutRequest(String serialNumber, String status, AlarmPanel alarmPanel) {

        try {
            HttpClient client = HttpClient.newHttpClient();


            String jsonBody = "{"
                    + "\"model\": \"" + alarmPanel.getModel() + "\","
                    + "\"status\": \"" + status + "\","
                    + "\"ipAddress\": \"" + alarmPanel.getIpAddress() + "\","
                    + "\"macAddress\": \"" + alarmPanel.getMacAddress() + "\","
                    + "\"account\": \"" + alarmPanel.getAccount() + "\","
                    + "\"serialNumber\": \"" + alarmPanel.getSerialNumber() + "\","
                    + "\"port\": \"" + alarmPanel.getPort() + "\""
                    + "}";

            System.out.println("jsonBody: " + jsonBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/alarms/serial/" + serialNumber))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status do envio de status da central: " + response.statusCode());
            System.out.println("Resposta do envio de status da central: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPortBySerialNumber(String serialNumber) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/alarms/serial/" + serialNumber))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readTree(jsonResponse).get("port").asInt();
            } else {
                System.out.println("Central não encontrada, usando porta padrão");
                return 6045;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 6045;
        }
    }

}
