/*
 * Copyright (c) 2021.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.channel.chatmanager;

import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftssystem.channel.Channel;
import de.ftscraft.ftssystem.channel.ChannelType;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import de.ftscraft.ftssystem.utils.hooks.EngineHook;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ChatManager {

    public final List<Channel> channels = new ArrayList<>();

    final FtsSystem plugin;

    public ChatManager(FtsSystem plugin) {
        this.plugin = plugin;
        loadChannels();
    }

    public abstract void chat(User u, String msg, Channel channel);

    public abstract void chat(User u, String msg);

    public Channel getChannel(String a) {
        for (Channel channel : channels) {
            if (channel.name().equalsIgnoreCase(a)) {
                return channel;
            }
            if (channel.prefix().equalsIgnoreCase(a)) {
                return channel;
            }
        }
        return null;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    void loadChannels() {
        File file = new File(plugin.getDataFolder() + "//channels.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            cfg.set("channel.global.prefix", "G");
            cfg.set("channel.global.default", false);
            cfg.set("channel.global.permission", "ftssystem.chat.global");
            cfg.set("channel.global.range", -1);
            cfg.set("channel.global.format", "§8[%cp]§r[%pr§7:§r%fa§r] %na: %msg");

            cfg.set("channel.local.prefix", "L");
            cfg.set("channel.local.default", true);
            cfg.set("channel.local.permission", "ftssystem.chat.local");
            cfg.set("channel.local.range", 40);
            cfg.set("channel.local.format", "[%pr§7:§r%fa§r] %na: %msg");

            try {
                cfg.save(file);
            } catch (IOException e) {
                plugin.getLogger().severe("IO Exception while loading channel");
            }
        }
        try {
            for (String a : cfg.getConfigurationSection("channel").getKeys(false)) {
                String prefix = cfg.getString("channel." + a + ".prefix");
                String permission = cfg.getString("channel." + a + ".permission");
                int range = cfg.getInt("channel." + a + ".range");
                String format = cfg.getString("channel." + a + ".format");
                ChannelType channelType = null;
                if (cfg.contains("channel." + a + ".type"))
                    channelType = ChannelType.valueOf(cfg.getString("channel." + a + ".type").toUpperCase());
                Channel c = new Channel(a, prefix, format, permission, range, channelType);
                channels.add(c);
            }
        } catch (Exception ex) {
            plugin.getLogger().severe("Exception while loading channel");
        }
    }

    public static String getUserDisplayName(User user, Channel channel) {
        Ausweis ausweis = EngineHook.getEngine().getAusweis(user.getPlayer());
        String name = user.getPlayer().getName();
        if (FtsSystem.Instance().getScoreboardManager().isInRoleplayMode(user.getPlayer())
                && ausweis != null
                && !channel.name().equalsIgnoreCase("Global")
                && !channel.name().equalsIgnoreCase("OOC")) {
            boolean deckname = user.isUsingDeckname();
            if (!deckname) {
                name = ausweis.getFirstName()
                        .replace(" ", "_") + "_" +
                        ausweis.getLastName().replace(" ", "_");
            } else {
                name = ausweis.getSpitzname().replace(" ", "_");
            }
        }
        return name;
    }

}
