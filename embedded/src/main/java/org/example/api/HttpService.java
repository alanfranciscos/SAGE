package org.example.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {

    public void makePostRequest(int usuario) {
        // https://www.twilio.com/pt-br/blog/5-maneiras-de-fazer-uma-chamada-http-em-java
        // https://stackoverflow.com/questions/75966165/how-to-replace-the-deprecated-url-constructors-in-java-20
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

            System.out.println("Status: " + response.statusCode());
            System.out.println("Resposta: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
