package miguel.nu.discordRelay.http;

import miguel.nu.discordRelay.Main;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestHandler {
    static HttpClient client = HttpClient.newHttpClient();

    public static void sendPost(String endpoint, String json){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Main.config.getString("domain") + "/" + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            Main.plugin.getLogger().severe("Could not send post request to discord bot");
            e.printStackTrace();
        }

    }
}
