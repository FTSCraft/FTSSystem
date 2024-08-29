package de.ftscraft.ftssystem.utils.hooks;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.UUID;

public class DiscordHook {

    private final String apiKey;
    private final String url = "localhost:5000/";

    public DiscordHook(String apiKey) {
        this.apiKey = apiKey;
    }

    public void sendNewTown(String town) {
        Unirest.post(url + "townchange")
                .header("accept", "application/json")
                .field("what", "creation")
                .field("town", town)
                .field("password", apiKey)
                .asJson();
    }

    public void sendDeleteTown(String town) {
        Unirest.post(url + "townchange")
                .header("accept", "application/json")
                .field("what", "deletion")
                .field("town", town)
                .field("password", apiKey)
                .asJson();
    }

    public void sendJoinedTown(String town, UUID user) {
        Unirest.post(url + "townchange")
                .header("accept", "application/json")
                .field("what", "town_join")
                .field("town", town)
                .field("uuid", user.toString())
                .field("password", apiKey)
                .asJson();
    }

    public void sendLeavedTown(String town, UUID user) {
        HttpResponse<JsonNode> response = Unirest.post(url + "townchange")
                .header("accept", "application/json")
                .field("what", "town_leave")
                .field("town", town)
                .field("uuid", user.toString())
                .field("password", apiKey)
                .asJson();
    }

    public void sendVerification(UUID user, String code, String town) {
        if (town == null) {
            Unirest.post(url + "verification")
                    .header("accept", "application/json")
                    .field("uuid", user.toString())
                    .field("code", code)
                    .field("password", apiKey)
                    .asJson();
        } else {
            Unirest.post(url + "verification")
                    .header("accept", "application/json")
                    .field("uuid", user.toString())
                    .field("code", code)
                    .field("town", town)
                    .field("password", apiKey)
                    .asJson();
        }

    }

}
