package de.ftscraft.ftssystem.utils.hooks;

import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.utils.PremiumManager;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class ForumHook {

    private String apiKey;
    private String apiUser;
    private int premiumGroupId;

    private final FtsSystem plugin;

    public ForumHook(FtsSystem plugin) {
        this.plugin = plugin;
    }

    public void updatePremiumGroup() throws UnirestException {

        PremiumManager premiumManager = plugin.getPremiumManager();
        ArrayList<String> usersInGroup = new ArrayList<>(Arrays.asList(getForumPremiumGroupMembers()));

        StringBuilder usersToAdd = new StringBuilder();
        ArrayList<String> premiumUsers = new ArrayList<>();

        for (UUID uuid : premiumManager.getPremiumPlayers().keySet()) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
            String name = op.getName();
            usersToAdd.append(name).append(",");
            premiumUsers.add(name);
        }

        StringBuilder usersToDelete = new StringBuilder();
        for (String name : usersInGroup) {
            if (!premiumUsers.contains(name)) {
                usersToDelete.append(name).append(",");
            }
        }

        if (!usersToAdd.isEmpty()) {
            usersToAdd.deleteCharAt(usersToAdd.length() - 1);
        }

        addUserToPremiumGroup(usersToAdd.toString());
        deleteUsersFromPremiumGroup(usersToDelete.toString());

    }

    public void deleteUsersFromPremiumGroup(String usernames) throws UnirestException {

        Unirest.delete("https://forum.ftscraft.de/groups/{X}/members.json".replace("{X}", String.valueOf(premiumGroupId)))
                .header("accept", "application/json")
                .header("Api-Username", apiUser)
                .header("Api-Key", apiKey)
                .field("usernames", usernames)
                .asJson();

    }

    public String[] getForumPremiumGroupMembers() throws UnirestException {

        HttpResponse<JsonNode> jsonResponse = Unirest.get("https://forum.ftscraft.de/groups/Premium/members.json").asJson();

        JSONObject obj = jsonResponse.getBody().getObject();
        JSONArray arr = obj.getJSONArray("members");

        String[] result = new String[arr.length()];

        for (int i = 0; i < arr.length(); i++) {
            result[i] = arr.getJSONObject(i).getString("username");
        }

        return result;

    }

    public void addUserToPremiumGroup(String username) throws UnirestException {

        Unirest.put("https://forum.ftscraft.de/groups/{X}/members.json".replace("{X}", String.valueOf(premiumGroupId)))
                .header("accept", "application/json")
                .header("Api-Username", apiUser)
                .header("Api-Key", apiKey)
                .field("usernames", username)
                .asJson();

    }

    public CheckCVResponse isAcceptedCV(String name, String url) throws UnirestException {

        HttpResponse<JsonNode> jsonGetResponse = Unirest.get(url + ".json")
                .header("accept", "application/json")
                .header("Api-Username", apiUser)
                .header("Api-Key", apiKey)
                .asJson();
        if (jsonGetResponse.getStatus() >= 400) {
            return CheckCVResponse.WRONG_URL;
        }

        JSONObject object = jsonGetResponse.getBody().getObject();
        JSONArray tags = object.getJSONArray("tags");

        JSONObject createdBy = object.getJSONObject("details").getJSONObject("created_by");
        String postedBy = createdBy.getString("username");

        if (tags == null || tags.isEmpty()) {
            return CheckCVResponse.NOT_ACCEPTED;
        }

        if (!tags.getString(0).equalsIgnoreCase("angenommen")) {
            return CheckCVResponse.NOT_ACCEPTED;
        }

        if (!name.equalsIgnoreCase(postedBy)) {
            return CheckCVResponse.NOT_FROM_PLAYER;
        }

        return CheckCVResponse.IS_ACCEPTED;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setApiUser(String apiUser) {
        this.apiUser = apiUser;
    }

    public void setPremiumGroupId(int premiumGroupId) {
        this.premiumGroupId = premiumGroupId;
    }

    public enum CheckCVResponse {
        WRONG_URL, NOT_FROM_PLAYER, NOT_ACCEPTED, IS_ACCEPTED
    }
}
