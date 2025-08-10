/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.configs;

import de.ftscraft.ftssystem.features.ResourcePackManager;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.travelsystem.TravelType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class ConfigManager {

    private ArrayList<String> autoMessages = new ArrayList<>();

    private boolean wartung = false;

    private final FtsSystem plugin;

    public ConfigManager(FtsSystem plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        FileConfiguration cfg = plugin.getConfig();
        //Defaults
        ArrayList<String> autoMessages = new ArrayList<>();
        autoMessages.add("§6Du möchtest den Server kostenlos unterstützen? §cHier kannst du voten: https://ftscraft.de/vote/");
        cfg.addDefault("messages", autoMessages);
        cfg.addDefault("latestPunishID", 0);
        cfg.addDefault("wartung", false);
        cfg.addDefault(ConfigVal.RESOURCE_PACK_LINK.getPath(), "https://ftscraft.de/wp-content/uploads/2025/08/FTSCraft-Texturepack.zip");
        cfg.addDefault(ConfigVal.RESOURCE_PACK_HASH.getPath(), "29a0c0627d968c4c4fea9e7288a147fdb5215b0f");
        for (TravelType value : TravelType.values()) {
            cfg.addDefault("travel_price." + value.name().toLowerCase(), value.getPrice());
        }
        cfg.options().copyDefaults(true);
        plugin.saveConfig();

        /*
         * Get Data
         */

        //AutoMessage
        cfg = plugin.getConfig();
        autoMessages.clear();
        autoMessages.addAll(cfg.getStringList("messages"));
        this.autoMessages = autoMessages;

        //Latest ID for Punishments

        wartung = cfg.getBoolean("wartung");
        for (TravelType value : TravelType.values()) {
            value.setPrice(cfg.getInt("travel_price." + value.name().toLowerCase()));
        }

        // Resource Pac
        ResourcePackManager.setResourcePack(
                cfg.getString(ConfigVal.RESOURCE_PACK_LINK.getPath()),
                cfg.getString(ConfigVal.RESOURCE_PACK_HASH.getPath())
        );

    }

    public void setConfig(ConfigVal val, Object obj) {
        plugin.getConfig().set(val.getPath(), obj);
    }

    public ArrayList<String> getAutoMessages() {
        return autoMessages;
    }

    public void save() {
        plugin.saveConfig();
    }

    public boolean isWartung() {
        return wartung;
    }
}

