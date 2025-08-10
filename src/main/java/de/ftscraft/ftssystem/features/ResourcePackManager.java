package de.ftscraft.ftssystem.features;

import de.ftscraft.ftssystem.configs.ConfigVal;
import de.ftscraft.ftssystem.main.FtsSystem;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.net.URI;

public class ResourcePackManager {

    private static String link;
    private static String hash;

    private static ResourcePackInfo packInfo;
    private static ResourcePackRequest packRequest;


    public static void sendResourcePack(final @NonNull Player target) {
        target.sendResourcePacks(packRequest);
    }

    public static void setResourcePack(String link, String hash) {
        ResourcePackManager.link = link;
        ResourcePackManager.hash = hash;

        generatePackInfo(link, hash);
        generateResourcePackRequest();
    }

    public static void saveResourcePackInfo() {
        FtsSystem.Instance().getConfigManager().setConfig(ConfigVal.RESOURCE_PACK_LINK, link);
        FtsSystem.Instance().getConfigManager().setConfig(ConfigVal.RESOURCE_PACK_HASH, hash);
    }

    private static void generatePackInfo(String link, String hash) {
        packInfo = ResourcePackInfo.resourcePackInfo()
                .uri(URI.create(link))
                .hash(hash)
                .build();
    }

    private static void generateResourcePackRequest() {
        packRequest = ResourcePackRequest.resourcePackRequest()
                .packs(packInfo)
                .prompt(Component.text("Bitte drücke auf Ja. " +
                        "Wenn du jetzt ablehnst musst du es entweder manuell herunterladen " +
                        "oder deine Einstellungen für den Server ändern."))
                .required(false)
                .build();
    }



}
