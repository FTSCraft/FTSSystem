/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.main;

import de.ftscraft.ftssystem.channel.Channel;
import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.menus.fts.FTSMenuInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

    private final FtsSystem plugin;
    private final Player player;
    private boolean scoreboardEnabled = true;
    private boolean approved = false;
    private boolean noobProtection = true;
    private boolean msgSound = true;
    private ChannelStatusSwitch disturbStatus = ChannelStatusSwitch.OFF;
    private ChannelStatusSwitch globalChannelStatus = ChannelStatusSwitch.ON;
    private ChannelStatusSwitch factionChannelStatus = ChannelStatusSwitch.ON;
    private ChannelStatusSwitch oocChannelStatus = ChannelStatusSwitch.ON;
    private Channel activeChannel;
    private final List<Channel> enabledChannels;
    private Location votehome;
    private boolean turnedServerMessagesOn;
    private FTSMenuInventory menu;
    private int forumId = -1;

    private final HashMap<Player, Integer> fights;

    public User(FtsSystem plugin, Player p) {
        this.player = p;
        this.plugin = plugin;
        enabledChannels = new ArrayList<>();
        fights = new HashMap<>();
        plugin.getUser().put(p.getName(), this);
        getData();
    }

    public Channel getActiveChannel() {
        return activeChannel;
    }

    public List<Channel> getEnabledChannels() {
        return enabledChannels;
    }

    public Player getPlayer() {
        return player;
    }

    public void save() {

        File file = new File(plugin.getDataFolder() + "//user//" + player.getUniqueId() + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        //Chat
        List<String> chNames = new ArrayList<>();

        for (Channel a : enabledChannels) {
            chNames.add(a.name());
        }

        cfg.set("channels", chNames.toArray());
        cfg.set("activeChannel", activeChannel.name());
        cfg.set("scoreboardOn", isScoreboardEnabled());
        cfg.set("approved", approved);
        cfg.set("msgSound", msgSound);
        cfg.set("turnedServerMessagesOn", turnedServerMessagesOn);
        //cfg.set("disturbStatus", getDisturbStatus().toString());
        cfg.set("oocStatus", getOocChannelStatus().toString());
        cfg.set("globalStatus", getGlobalChannelStatus().toString());
        cfg.set("factionStatus", getFactionChannelStatus().toString());
        cfg.set("forumId", forumId);

        cfg.set("noobschutz", noobProtection);
        if (votehome != null) {
            cfg.set("votehome.x", votehome.getX());
            cfg.set("votehome.y", votehome.getY());
            cfg.set("votehome.z", votehome.getZ());
            cfg.set("votehome.world", votehome.getWorld().getName());
            cfg.set("votehome.pitch", votehome.getPitch());
            cfg.set("votehome.yaw", votehome.getYaw());
        }

        //Punishment


        try {
            cfg.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("IO Exception while saving user data");
        }
    }

    private void getData() {
        File file = new File(plugin.getDataFolder() + "//user//" + player.getUniqueId() + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {

            player.performCommand("tutorialbook");

            for (Player a : Bukkit.getOnlinePlayers()) {
                a.sendMessage("§cDer Spieler §e" + player.getName() + " §cist das 1. mal hier. Sagt Hallo!");
            }


        }

        for (Channel a : plugin.getChatManager().getChannels()) {
            if (player.hasPermission(a.permission())) {
                enabledChannels.add(a);
            }
        }


        this.activeChannel = plugin.getChatManager().getChannel(cfg.getString("activeChannel"));
        if (this.activeChannel == null)
            this.activeChannel = plugin.getChatManager().getChannel("Local");

        this.scoreboardEnabled = !cfg.contains("scoreboardOn") || cfg.getBoolean("scoreboardOn");
        this.noobProtection = !cfg.contains("noobschutz") || cfg.getBoolean("noobschutz");
        this.msgSound = cfg.contains("msgSound") && cfg.getBoolean("msgSound");
        this.approved = cfg.contains("approved") && cfg.getBoolean("approved");
        if (cfg.contains("turnedServerMessagesOn")) {
            this.turnedServerMessagesOn = cfg.getBoolean("turnedServerMessagesOn");
        } else this.turnedServerMessagesOn = true;

        if (cfg.contains("oocStatus")) {
            this.oocChannelStatus = ChannelStatusSwitch.valueOf(cfg.getString("oocStatus"));
        }
        if (cfg.contains("factionStatus")) {
            this.factionChannelStatus = ChannelStatusSwitch.valueOf(cfg.getString("factionStatus"));
        }
        if (cfg.contains("globalStatus")) {
            this.globalChannelStatus = ChannelStatusSwitch.valueOf(cfg.getString("globalStatus"));
        }
        if (cfg.contains("forumId")) {
            this.forumId = cfg.getInt("forumId");
        }

        if (cfg.contains("votehome.x")) {
            Location votehome = new Location(
                    Bukkit.getWorld(cfg.getString("votehome.world")),
                    cfg.getDouble("votehome.x"),
                    cfg.getDouble("votehome.y"),
                    cfg.getDouble("votehome.z"),
                    Float.parseFloat(cfg.getString("votehome.yaw")),
                    Float.parseFloat(cfg.getString("votehome.pitch"))
            );
            setVotehome(votehome);
        }

    }

    public void joinChannel(Channel channel) {
        if (player.hasPermission(channel.permission())) {
            enabledChannels.add(channel);
            if (activeChannel == null)
                activeChannel = channel;
        }
    }

    public void leaveChannel(Channel channel) {
        enabledChannels.remove(channel);
        if (activeChannel == channel)
            activeChannel = null;
    }

    public void setActiveChannel(Channel activeChannel) {
        if (player.hasPermission(activeChannel.permission())) {
            this.activeChannel = activeChannel;
            if (!enabledChannels.contains(activeChannel))
                enabledChannels.add(activeChannel);
        }
    }

    public void toggleChannel(Channel channel) {
        if (enabledChannels.contains(channel)) {
            leaveChannel(channel);
            player.sendMessage(Messages.LEFT_CHANNEL.replace("%s", channel.name()));
        } else {
            setActiveChannel(channel);
            player.sendMessage(Messages.NOW_ACTIVE_CHANNEL.replace("%s", channel.name()));
        }
    }

    public boolean isScoreboardEnabled() {
        return scoreboardEnabled;
    }

    public void setScoreboardEnabled(boolean scoreboardEnabled) {
        this.scoreboardEnabled = scoreboardEnabled;
    }

    public HashMap<Player, Integer> getFights() {
        return fights;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean b) {
        approved = b;
    }

    public boolean turnedServerMessagesOn() {
        return turnedServerMessagesOn;
    }

    public void setTurnedServerMessagesOn(boolean turnedServerMessagesOn) {
        this.turnedServerMessagesOn = turnedServerMessagesOn;
    }

    public void setMsgSound(boolean msgSound) {
        this.msgSound = msgSound;
    }

    public boolean isMsgSoundEnabled() {
        return msgSound;
    }

    public boolean hasNoobProtection() {
        return noobProtection;
    }

    public void setNoobProtection(boolean noobProtection) {
        this.noobProtection = noobProtection;
    }

    public void setMenu(FTSMenuInventory menu) {
        this.menu = menu;
    }

    public void refreshMenu() {
        menu.refresh();
    }

    public void openMenu() {
        if (menu == null) {
            menu = new FTSMenuInventory(player, plugin);
        }
        menu.refresh();
        player.openInventory(menu.getInventory());
    }

    public enum ChannelStatusSwitch {
        ON, OFF, RP
    }

    public ChannelStatusSwitch getDisturbStatus() {
        return disturbStatus;
    }

    public void setDisturbStatus(ChannelStatusSwitch disturbStatus) {
        this.disturbStatus = disturbStatus;
    }

    public boolean isDisturbable() {

        return disturbStatus == ChannelStatusSwitch.OFF;

    }

    public Location getVotehome() {
        return votehome;
    }

    public void setVotehome(Location votehome) {
        this.votehome = votehome;
    }

    public boolean hasVotehome() {
        return votehome != null;
    }

    public ChannelStatusSwitch getGlobalChannelStatus() {
        return globalChannelStatus;
    }

    public ChannelStatusSwitch getFactionChannelStatus() {
        return factionChannelStatus;
    }

    public ChannelStatusSwitch getOocChannelStatus() {
        return oocChannelStatus;
    }

    public void setGlobalChannelStatus(ChannelStatusSwitch globalChannelStatus) {
        this.globalChannelStatus = globalChannelStatus;
    }

    public void setFactionChannelStatus(ChannelStatusSwitch factionChannelStatus) {
        this.factionChannelStatus = factionChannelStatus;
    }

    public void setOocChannelStatus(ChannelStatusSwitch oocChannelStatus) {
        this.oocChannelStatus = oocChannelStatus;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }
}
